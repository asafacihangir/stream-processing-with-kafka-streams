package com.phoenix.gaming;

import com.github.javafaker.Faker;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GamingPlayerService {

  private final Faker faker;
  private final KafkaTemplate kafkaTemplate;

  public GamingPlayerService(Faker faker, KafkaTemplate kafkaTemplate) {
    this.faker = faker;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendScores() {

    // Define list of players
    List<String> players = new ArrayList<String>();
    players.add("Bob");
    players.add("Mike");
    players.add("Kathy");
    players.add("Sam");

    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      // Generate a random player & score
      final String player = players.get(random.nextInt(players.size()));
      final int score = random.nextInt(100) + 1;

      final GamingPlayerScore gamingPlayerScore = new GamingPlayerScore(player, score);
      this.kafkaTemplate.send("streaming.leaderboards.input", player, gamingPlayerScore);
    }
  }
}
