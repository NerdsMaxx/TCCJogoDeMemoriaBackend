package com.tcc.app.web.memory_game.api.dtos.responses;

import java.time.LocalDateTime;

public record PlayerResultResponseDto(String creator,
                                      String player,
                                      Integer score,
                                      Integer numberRightOptions,
                                      Integer numberWrongOptions,
                                      Integer numberAttempts,
                                      LocalDateTime startTime,
                                      LocalDateTime endTime) {
}