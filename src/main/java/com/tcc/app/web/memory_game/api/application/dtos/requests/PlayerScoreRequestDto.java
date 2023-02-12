package com.tcc.app.web.memory_game.api.application.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PlayerScoreRequestDto(@NotBlank  String code,
                                    @Min(0) Integer score,
                                    @Min(0) Integer numberCardCorrect,
                                    @Min(0) Integer numberCardWrong) {
}