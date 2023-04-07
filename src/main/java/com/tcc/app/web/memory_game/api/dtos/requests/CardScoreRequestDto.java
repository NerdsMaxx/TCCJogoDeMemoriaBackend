package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CardScoreRequestDto(@NotNull @Min(1) Long id,
                                  @NotNull Boolean winner) {
}