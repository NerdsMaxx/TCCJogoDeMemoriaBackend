package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDto(@NotBlank String username, @NotBlank String password) {

}