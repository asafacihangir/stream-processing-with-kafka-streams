package com.phoenix.analytics.messaging;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GamingPlayerScoreService {

  public static final String redisKey = "player-leaderboard";
  private final RedisTemplate redisTemplate;

  public GamingPlayerScoreService(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void updateScore(String playerName, double count) {
    redisTemplate.opsForZSet().incrementScore(redisKey, playerName, count);
  }
}
