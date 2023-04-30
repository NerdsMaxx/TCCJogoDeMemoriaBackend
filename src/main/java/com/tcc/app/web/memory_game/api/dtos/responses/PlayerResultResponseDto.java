package com.tcc.app.web.memory_game.api.dtos.responses;

public record PlayerResultResponseDto(String player,
                                      Integer score,
                                      Integer numberRightOptions,
                                      Integer numberWrongOptions,
                                      Integer numberAttempts) {
}