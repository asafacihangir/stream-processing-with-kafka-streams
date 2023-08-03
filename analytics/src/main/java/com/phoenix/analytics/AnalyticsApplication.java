package com.phoenix.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
@EnableJpaAuditing
public class AnalyticsApplication {

  public static void main(String[] args) {
    SpringApplication.run(AnalyticsApplication.class, args);
  }
}
