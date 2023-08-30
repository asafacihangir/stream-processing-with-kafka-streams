package com.phoenix.analytics.messaging;


public class GamingPlayerScore {

  private String playerName;
  private int score;
  public GamingPlayerScore() {
  }

  public GamingPlayerScore(String playerName, int score) {
    this.playerName = playerName;
    this.score = score;
  }

  public String getPlayerName() {
    return playerName;
  }

  public int getScore() {
    return score;
  }
}
