package com.tcc.app.web.memory_game.api.application.dtos.responses;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses.UserDetailsDto;

import java.util.Set;

public record MemoryGameDetailsDto( UserDetailsDto user,
                                    String name,
                                    Set<String> subjectSet,
                                    Set<CardDetailsDto> cardSet ) {
    
}