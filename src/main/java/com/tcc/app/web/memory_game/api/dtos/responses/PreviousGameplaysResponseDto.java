package com.tcc.app.web.memory_game.api.dtos.responses;

public record PreviousGameplaysResponseDto(String memoryGame,
                                           String creator,
                                           Integer score,
                                           Integer numberRightOptions,
                                           Integer numberWrongOptions,
                                           Integer numberAttempts) {
}