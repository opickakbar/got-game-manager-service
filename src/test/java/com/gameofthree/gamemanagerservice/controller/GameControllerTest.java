package com.gameofthree.gamemanagerservice.controller;

import com.gameofthree.gamemanagerservice.service.GameManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GameControllerTest {

    @Mock
    private GameManagerService gameManagerService;

    @InjectMocks
    private GameController gameController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void testStartGame_Success() throws Exception {
        // given game starts successfully
        when(gameManagerService.startGame()).thenReturn("Game Started with number: 42");

        // when calling the API
        mockMvc.perform(post("/games/start")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Game Started with number: 42"));

        // then verify the service call
        verify(gameManagerService, times(1)).startGame();
    }

    @Test
    void testResultGame_WinnerExists() throws Exception {
        // given a winner exists
        when(gameManagerService.resultGame()).thenReturn("Winner is Player1");

        // when calling the API
        mockMvc.perform(get("/games/result")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Winner is Player1"));

        // then verify the service call
        verify(gameManagerService, times(1)).resultGame();
    }

    @Test
    void testResultGame_NoWinnerYet() throws Exception {
        // given no winner is set
        when(gameManagerService.resultGame()).thenReturn("Game is not finished yet!");

        // when calling the API
        mockMvc.perform(get("/games/result")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Game is not finished yet!"));

        // then verify the service call
        verify(gameManagerService, times(1)).resultGame();
    }
}
