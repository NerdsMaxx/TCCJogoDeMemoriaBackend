package com.tcc.app.web.memory_game.api.application.dtos.requests.update;

import java.util.Set;

public record MemoryGameUpdateDto(
        String name,
        Set<CardUpdateDto> cardSet,
        Set<String> subjectSet
) {
}