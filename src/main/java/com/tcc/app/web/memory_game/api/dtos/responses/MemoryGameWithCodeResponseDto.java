package com.tcc.app.web.memory_game.api.dtos.responses;

public record MemoryGameWithCodeResponseDto(String creator,
                                            String name,
                                            String code) {
}