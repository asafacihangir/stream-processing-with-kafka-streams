package com.phoenix.gaming;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gaming-player")
public class GamingPlayerController {

  private final GamingPlayerService gamingPlayerService;

  public GamingPlayerController(GamingPlayerService gamingPlayerService) {
    this.gamingPlayerService = gamingPlayerService;
  }

  @PostMapping("/send-scores")
  public void sendScores() {
    gamingPlayerService.sendScores();
  }
}
