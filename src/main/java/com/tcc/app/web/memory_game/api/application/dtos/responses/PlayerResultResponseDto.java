package com.tcc.app.web.memory_game.api.application.dtos.responses;

public record PlayerResultResponseDto(String player,
                                      Integer score,
                                      Integer numberPairCardCorrect,
                                      Integer numberPairCardWrong) {
}