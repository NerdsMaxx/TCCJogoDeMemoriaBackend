package com.tcc.app.web.memory_game.api.application.dtos.responses;

import java.util.Set;

public record GameplayFinishedDtoList(String memoryGame,
                                      String creator,
                                      Set<PlayerGameplayFinsishedResponseDto> playerSet,
                                      Boolean allFinished) {
}