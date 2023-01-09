package com.tcc.app.web.memory_game.api.application.dtos.requests.update;

import jakarta.validation.constraints.NotBlank;

public record CardUpdateDto(
        @NotBlank String firstContent,
        @NotBlank String secondContent
) {
}