package com.tcc.app.web.memory_game.api.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record GameplayRequestDto(@NotBlank String memoryGame,
                                 @NotBlank String creator) {
}