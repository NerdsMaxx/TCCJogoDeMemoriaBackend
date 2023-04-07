package com.tcc.app.web.memory_game.api.dtos.responses;

public record PlayerAddedResponseDto(String player,
                                     MemoryGameResponseDto memoryGame) {
}