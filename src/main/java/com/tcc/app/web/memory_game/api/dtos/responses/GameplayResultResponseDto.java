package com.tcc.app.web.memory_game.api.dtos.responses;

import java.time.LocalDateTime;
import java.util.Set;

public record GameplayResultResponseDto(String memoryGame,
                                        String creator,
                                        Set<PlayerResultResponseDto> playerSet,
                                        Boolean noPlayers) {
}