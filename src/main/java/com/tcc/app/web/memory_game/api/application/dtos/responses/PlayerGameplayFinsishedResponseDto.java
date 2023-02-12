package com.tcc.app.web.memory_game.api.application.dtos.responses;

public record PlayerGameplayFinsishedResponseDto( String player,
                                                  String memoryGame,
                                                  String creator,
                                                  Integer score,
                                                  Integer numberCardCorrect,
                                                  Integer numberCardWrong,
                                                  Boolean allFinished ) {
}