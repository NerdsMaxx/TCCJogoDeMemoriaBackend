package com.tcc.app.web.memory_game.api.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record MemoryGameRequestDto( @NotBlank String name,
                                    Set<String> subjectSet,
                                    @NotEmpty Set<CardRequestDto> cardSet) {
    
}