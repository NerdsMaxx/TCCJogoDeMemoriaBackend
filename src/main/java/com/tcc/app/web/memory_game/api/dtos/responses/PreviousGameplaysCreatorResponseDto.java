package com.tcc.app.web.memory_game.api.dtos.responses;

import java.time.LocalDateTime;

public record PreviousGameplaysCreatorResponseDto(Long gameplayId,
                                                  String memoryGame,
                                                  String usedCode,
                                                  Integer numbersPlayer,
                                                  LocalDateTime startTime,
                                                  LocalDateTime lastTime) {
}