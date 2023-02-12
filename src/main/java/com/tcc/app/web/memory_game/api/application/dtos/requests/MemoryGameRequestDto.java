package com.tcc.app.web.memory_game.api.application.dtos.requests;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MemoryGameRequestDto( @NotBlank String name,
                                    List<String> subjectList,
                                    @NotEmpty List<CardRequestDto> cardList) {
    
}