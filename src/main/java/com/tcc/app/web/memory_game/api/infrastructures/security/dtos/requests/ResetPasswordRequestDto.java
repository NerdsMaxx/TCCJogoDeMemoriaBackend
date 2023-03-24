package com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(@NotBlank String username,
                                      @NotBlank String newPassword) {
}