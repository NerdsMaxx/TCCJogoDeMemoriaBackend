package com.tcc.app.web.memory_game.api.application.dtos.responses;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses.UserResponseDto;

import java.util.Set;

public record MemoryGameResponseDto(UserResponseDto user,
                                    String name,
                                    Set<String> subjectSet,
                                    Set<CardResponseDto> cardSet) {
    
}