package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.Min;

public record PlayerScoreRequestDto(Integer score) {}