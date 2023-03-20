package com.tcc.app.web.memory_game.api.application.dtos.responses;

import java.util.Set;

public record GameplayResultResponseDto(String memoryGame,
                                        String creator,
                                        Set<PlayerResultResponseDto> playerSet,
                                        Boolean allFinished) {
}