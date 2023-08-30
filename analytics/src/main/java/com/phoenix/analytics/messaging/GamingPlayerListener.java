package com.phoenix.analytics.messaging;

import com.phoenix.analytics.common.ClassDeSerializer;
import com.phoenix.analytics.common.ClassSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GamingPlayerListener {

  private final GamingPlayerScoreService gamingPlayerScoreService;

  public GamingPlayerListener(GamingPlayerScoreService gamingPlayerScoreService) {
    this.gamingPlayerScoreService = gamingPlayerScoreService;
  }

  @Bean
  public KStream<String, GamingPlayerScore> salesOrderKStream(StreamsBuilder builder) {
    final Serde<String> stringSerde = Serdes.String();
    final Serde<GamingPlayerScore> gamingPlayerScoreSerde =
        Serdes.serdeFrom(new ClassSerializer<>(), new ClassDeSerializer<>(GamingPlayerScore.class));

    // Create the source node for Orders
    final KStream<String, GamingPlayerScore> gamingInput =
        builder.stream(
            "streaming.leaderboards.input", Consumed.with(stringSerde, gamingPlayerScoreSerde));

    gamingInput.peek(
        (player, score) ->
            System.out.println("Received Score : Player = " + player + ", Score = " + score));

    // Update the Redis key with the new score increment
    gamingInput.foreach(
        (product, score) -> gamingPlayerScoreService.updateScore(product, score.getScore()));

    return gamingInput;
  }
}
