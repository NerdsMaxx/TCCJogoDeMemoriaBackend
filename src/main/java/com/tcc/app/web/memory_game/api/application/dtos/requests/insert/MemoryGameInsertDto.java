package com.tcc.app.web.memory_game.api.application.dtos.requests.insert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record MemoryGameInsertDto( @NotBlank String name,
                                   Set<String> subjectSet,
                                   @NotEmpty Set<CardInsertDto> cardSet ) {
    
}