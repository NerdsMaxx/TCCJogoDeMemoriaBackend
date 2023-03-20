package com.tcc.app.web.memory_game.api.application.dtos.responses;

import java.util.Set;

public record MemoryGameCardsResponseDto(String creator,
                                         String name,
                                         Set<String> subjectSet,
                                         Set<CardResponseDto> cardSet) {
}