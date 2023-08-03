package com.phoenix.order;

import com.github.javafaker.Faker;
import java.math.BigDecimal;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final Faker faker;
  private final KafkaTemplate kafkaTemplate;

  public OrderService(Faker faker, KafkaTemplate kafkaTemplate) {
    this.faker = faker;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendOrders() {
    int orderId = (int) Math.floor((double) System.currentTimeMillis() / 1000);
    // Generate 100 sample order records
    for (int i = 0; i < 100; i++) {
      final SalesOrder so = new SalesOrder();
      so.setOrderId(orderId);
      so.setPrice(new BigDecimal(faker.commerce().price()));
      so.setProduct(faker.commerce().productName());
      so.setQuantity(faker.number().numberBetween(1, 500));

      this.kafkaTemplate.send("streaming.orders.input", String.valueOf(orderId), so);

      orderId = orderId + 1;
    }
  }
}
