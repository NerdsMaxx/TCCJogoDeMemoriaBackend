package com.tcc.app.web.memory_game.api.application.dtos.responses;

public record PlayerAddedResponseDto(String player,
                                     String memoryGame,
                                     String creator) {
}