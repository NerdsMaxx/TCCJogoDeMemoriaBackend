package com.tcc.app.web.memory_game.api.application.dtos.responses;

public record PlayerGameplayFinsishedResponseDto( String player,
                                                  Integer score,
                                                  Integer numberCardCorrect,
                                                  Integer numberCardWrong) {
}