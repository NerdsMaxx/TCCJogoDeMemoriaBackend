package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.responses.GameplayResponseDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.PlayerAddedResponseDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.PlayerGameplayFinsishedResponseDto;
import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameplayMapper {
    
    GameplayResponseDto toGameplayResponseDto(CodeGameplayEntity codeGameplay);
    
    
    @Mapping(source = "playerGameplay.player.user.username", target = "player")
    @Mapping(source = "playerGameplay.gameplay.memoryGame.memoryGame", target = "memoryGame")
    @Mapping(source = "playerGameplay.gameplay.memoryGame.creator.user.username", target = "creator")
    PlayerAddedResponseDto toPlayerAddedResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "playerGameplay.player.user.username", target = "player")
    @Mapping(source = "playerGameplay.gameplay.memoryGame.memoryGame", target = "memoryGame")
    @Mapping(source = "playerGameplay.gameplay.memoryGame.creator.user.username", target = "creator")
    @Mapping(expression = "java(codeGameplay.getNumbersPlayerMoment() == 0)", target = "allFinished")
    PlayerGameplayFinsishedResponseDto toPlayerGameplayFinsishedResponseDto(PlayerGameplayEntity playerGameplay, CodeGameplayEntity codeGameplay);

}