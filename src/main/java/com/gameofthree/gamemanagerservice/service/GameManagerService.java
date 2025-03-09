package com.gameofthree.gamemanagerservice.service;

import com.gameofthree.gamemanagerservice.dto.GameMoveEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.gameofthree.gamemanagerservice.util.Utils.GAME_MASTER_ID;
import static com.gameofthree.gamemanagerservice.util.Utils.PLAYER_1_ID;
import static com.gameofthree.gamemanagerservice.util.Utils.PLAYER_2_ID;
import static com.gameofthree.gamemanagerservice.util.Utils.WINNER_ID;

@Service
@Slf4j
public class GameManagerService {

    private final RabbitTemplate rabbitTemplate;
    private final GameRedisManager gameRedisManager;

    public GameManagerService(RabbitTemplate rabbitTemplate, GameRedisManager gameRedisManager) {
        this.rabbitTemplate = rabbitTemplate;
        this.gameRedisManager = gameRedisManager;
    }

    public String startGame() {
        int initialNumberResult = (int) (Math.random() * 100) + 10;
        int initialNumberAdded = 0;
        String player1Id = gameRedisManager.getValue(PLAYER_1_ID);
        String player2Id = gameRedisManager.getValue(PLAYER_2_ID);
        if (player1Id == null || player2Id == null) {
            return "Error: All players have not registered yet!";
        }
        rabbitTemplate.convertAndSend("game.exchange", "player.1", new GameMoveEventDto(initialNumberAdded, initialNumberResult, GAME_MASTER_ID, PLAYER_1_ID));
        return "Game Started with number: " + initialNumberResult;
    }

    public String resultGame() {
        String winner = gameRedisManager.getValue(WINNER_ID);
        return (winner != null) ? "Winner is " + winner : "Game is not finished yet!";
    }
}
