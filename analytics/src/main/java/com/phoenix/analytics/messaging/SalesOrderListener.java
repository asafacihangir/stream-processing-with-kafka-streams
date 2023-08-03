package com.phoenix.analytics.messaging;

import com.phoenix.analytics.common.ClassDeSerializer;
import com.phoenix.analytics.common.ClassSerializer;
import com.phoenix.analytics.domain.OrderSummary;
import com.phoenix.analytics.repository.OrderSummaryRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SalesOrderListener {

  private final OrderSummaryRepository orderSummaryRepository;

  public SalesOrderListener(OrderSummaryRepository orderSummaryRepository) {
    this.orderSummaryRepository = orderSummaryRepository;
  }

  @Bean
  public KStream<String, SalesOrder> salesOrderKStream(StreamsBuilder builder) {
    final Serde<String> stringSerde = Serdes.String();
    final Serde<SalesOrder> orderSerde =
        Serdes.serdeFrom(new ClassSerializer<>(), new ClassDeSerializer<>(SalesOrder.class));
    final Serde<OrderAggregator> aggregatorSerde =
        Serdes.serdeFrom(new ClassSerializer<>(), new ClassDeSerializer<>(OrderAggregator.class));

    // Create the source node for Orders
    final KStream<String, SalesOrder> orderObjects =
        builder.stream("streaming.orders.input", Consumed.with(stringSerde, orderSerde));

    // Print objects received
    orderObjects.peek((key, value) -> System.out.println("Received Order : " + value));

    // Create a window of 5 seconds
    TimeWindows tumblingWindow = TimeWindows.of(Duration.ofSeconds(5)).grace(Duration.ZERO);

    // Initializer creates a new aggregator for every
    // Window & Product combination
    Initializer<OrderAggregator> orderAggregatorInitializer = OrderAggregator::new;

    // Aggregator - Compute total value and call the aggregator
    Aggregator<String, SalesOrder, OrderAggregator> orderAdder =
        (key, value, aggregate) ->
            aggregate.add(value.getPrice().multiply(BigDecimal.valueOf(value.getQuantity())));

    // Perform Aggregation
    KTable<Windowed<String>, OrderAggregator> productSummary =
        orderObjects
            .groupBy( // Group by Product
                (key, value) -> value.getProduct(), Grouped.with(stringSerde, orderSerde))
            .windowedBy(tumblingWindow)
            .aggregate(
                orderAggregatorInitializer,
                orderAdder,
                // Store output in a materialized store
                Materialized.<String, OrderAggregator, WindowStore<Bytes, byte[]>>as(
                        "time-windowed-aggregate-store")
                    .withValueSerde(aggregatorSerde))
            .suppress(
                Suppressed.untilWindowCloses(
                    Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

    productSummary
        .toStream() // convert KTable to KStream
        .foreach(
            (key, aggregation) -> {
              System.out.println(
                  "Received Summary :"
                      + " Window = "
                      + key.window().startTime()
                      + " Product ="
                      + key.key()
                      + " Value = "
                      + aggregation.getTotalValue());

              final OrderSummary orderSummary = new OrderSummary();
              orderSummary.setProduct(key.key());
              orderSummary.setTotalValue(aggregation.getTotalValue());
              orderSummary.setIntervalTimestamp(LocalDateTime.now());

              this.orderSummaryRepository.save(orderSummary);
            });

    return orderObjects;
  }
}
