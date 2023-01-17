package com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(@NotBlank String name,
                             @NotBlank String username,
                             @NotBlank String email,
                             @NotBlank String password,
                             @NotBlank String type) {
}