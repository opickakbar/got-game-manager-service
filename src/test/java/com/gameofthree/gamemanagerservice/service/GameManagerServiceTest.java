package com.gameofthree.gamemanagerservice.service;

import com.gameofthree.gamemanagerservice.dto.GameMoveEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

import static com.gameofthree.gamemanagerservice.util.Utils.GAME_EXCHANGE;
import static com.gameofthree.gamemanagerservice.util.Utils.GAME_MASTER_ID;
import static com.gameofthree.gamemanagerservice.util.Utils.PLAYER_1_ID;
import static com.gameofthree.gamemanagerservice.util.Utils.PLAYER_1_ROUTING_KEY;
import static com.gameofthree.gamemanagerservice.util.Utils.PLAYER_2_ID;
import static com.gameofthree.gamemanagerservice.util.Utils.WINNER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameManagerServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private GameRedisManager gameRedisManager;

    @InjectMocks
    private GameManagerService gameManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartGame_Success() {
        // Given both players are registered
        when(gameRedisManager.getValue(PLAYER_1_ID)).thenReturn(PLAYER_1_ID);
        when(gameRedisManager.getValue(PLAYER_2_ID)).thenReturn(PLAYER_2_ID);

        // When starting the game
        String response = gameManagerService.startGame();

        // Then ensure a message is sent to Player 1
        ArgumentCaptor<GameMoveEventDto> eventCaptor = ArgumentCaptor.forClass(GameMoveEventDto.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq(GAME_EXCHANGE), eq(PLAYER_1_ROUTING_KEY), eventCaptor.capture());

        GameMoveEventDto publishedEvent = eventCaptor.getValue();
        assertEquals(GAME_MASTER_ID, publishedEvent.getFromPlayerId());
        assertEquals(PLAYER_1_ID, publishedEvent.getToPlayerId());
        assertTrue(response.startsWith("Game Started with number: "));
    }

    @Test
    void testStartGame_Failure_NoPlayersRegistered() {
        // Given no players are registered
        when(gameRedisManager.getValue(PLAYER_1_ID)).thenReturn(null);
        when(gameRedisManager.getValue(PLAYER_2_ID)).thenReturn(null);

        // When starting the game
        String response = gameManagerService.startGame();

        // Then the game should not start
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));
        assertEquals("Error: All players have not registered yet!", response);
    }

    @Test
    void testResultGame_GameFinished() {
        // Given a winner is stored in Redis
        when(gameRedisManager.getValue(WINNER_ID)).thenReturn(PLAYER_1_ID);

        // When checking the result
        String result = gameManagerService.resultGame();

        // Then the correct winner should be displayed
        assertEquals("Winner is Player1", result);
    }

    @Test
    void testResultGame_GameNotFinished() {
        // Given no winner is stored in Redis
        when(gameRedisManager.getValue(WINNER_ID)).thenReturn(null);

        // When checking the result
        String result = gameManagerService.resultGame();

        // Then it should indicate the game is not finished
        assertEquals("Game is not finished yet!", result);
    }
}

