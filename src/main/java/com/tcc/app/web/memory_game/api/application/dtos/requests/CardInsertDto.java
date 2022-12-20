package com.tcc.app.web.memory_game.api.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CardInsertDto(@NotBlank String firstContent, @NotBlank String secondContent) {

}
