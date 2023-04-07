package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CardRequestDto( @NotBlank String firstContent,
                              @NotBlank String secondContent ) {
    
}