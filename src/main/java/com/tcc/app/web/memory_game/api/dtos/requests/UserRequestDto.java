package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserRequestDto(@NotBlank String name,
                             @NotBlank String username,
                             @NotBlank @Email String email,
                             @NotBlank String password,
                             @NotEmpty Set<String> type) {
}