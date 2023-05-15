package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlayerScoreRequestDto(@NotNull Integer score,
                                    @Min(0) Integer numberRightOptions,
                                    @Min(0) Integer numberWrongOptions,
                                    @Min(0) Integer numberAttempts) {}