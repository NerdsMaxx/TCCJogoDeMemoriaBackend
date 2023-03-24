package com.tcc.app.web.memory_game.api.application.dtos.responses;

public record MemoryGameWithCodeResponseDto(String creator,
                                            String name,
                                            String code) {
}