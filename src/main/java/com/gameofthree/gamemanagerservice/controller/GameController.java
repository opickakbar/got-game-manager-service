package com.gameofthree.gamemanagerservice.controller;

import com.gameofthree.gamemanagerservice.service.GameManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {

    private final GameManagerService gameManagerService;

    public GameController(GameManagerService gameManagerService) {
        this.gameManagerService = gameManagerService;
    }

    @PostMapping("/start")
    public String startGame() {
        return gameManagerService.startGame();
    }

    @GetMapping("/result")
    public String resultGame() {
        return gameManagerService.resultGame();
    }

}
