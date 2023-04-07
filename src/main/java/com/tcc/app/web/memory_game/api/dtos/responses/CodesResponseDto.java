package com.tcc.app.web.memory_game.api.dtos.responses;

import java.util.Set;

public record CodesResponseDto(Set<MemoryGameWithCodeResponseDto> codes) {
}