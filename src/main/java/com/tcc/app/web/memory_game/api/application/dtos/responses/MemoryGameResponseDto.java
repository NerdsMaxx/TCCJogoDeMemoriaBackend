package com.tcc.app.web.memory_game.api.application.dtos.responses;

import com.tcc.app.web.memory_game.api.application.dtos.responses.CardResponseDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses.UserResponseDto;

import java.util.List;

public record MemoryGameResponseDto(UserResponseDto user,
                                    String name,
                                    List<String> subjectList,
                                    List<CardResponseDto> cardList) {
    
}