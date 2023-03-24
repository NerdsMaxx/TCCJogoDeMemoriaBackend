package com.tcc.app.web.memory_game.api.application.dtos.responses;

public record PreviousGameplaysResponseDto(String memoryGame,
                                           String creator,
                                           Integer score) {
}