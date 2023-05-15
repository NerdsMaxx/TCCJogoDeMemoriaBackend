package com.tcc.app.web.memory_game.api.dtos.responses;

import java.time.LocalDateTime;

public record PreviousGameplaysPlayerResponseDto(String memoryGame,
                                                 String player,
                                                 String creator,
                                                 Integer score,
                                                 Integer numberRightOptions,
                                                 Integer numberWrongOptions,
                                                 Integer numberAttempts,
                                                 LocalDateTime startTime,
                                                 LocalDateTime endTime) {
}