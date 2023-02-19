package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.GameplayFinishedDtoList;
import com.tcc.app.web.memory_game.api.application.dtos.responses.GameplayResponseDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.PlayerAddedResponseDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.PlayerGameplayFinsishedResponseDto;
import com.tcc.app.web.memory_game.api.application.entities.CardGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(uses = {MemoryGameMapper.class})
@DecoratedWith(GameplayMapperDecorator.class)
public interface GameplayMapper {
    
    GameplayResponseDto toGameplayResponseDto(CodeGameplayEntity codeGameplay);
    
    
    @Mapping(source = "player.user.username", target = "player")
    @Mapping(source = "gameplay.memoryGame", target = "memoryGame")
    PlayerAddedResponseDto toPlayerAddedResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "player.user.username", target = "player")
    PlayerGameplayFinsishedResponseDto toPlayerGameplayFinsishedResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "playerGameplaySet", target = "playerSet")
    @Mapping(expression = "java(codeGameplay.getNumbersPlayerMoment() == 0)", target = "allFinished")
    GameplayFinishedDtoList toGameplayFinishedDtoList(Set<PlayerGameplayEntity> playerGameplaySet,
                                                      CodeGameplayEntity codeGameplay,
                                                      String memoryGame,
                                                      String creator);
    
    PlayerGameplayEntity updatePlayerGameplay(PlayerScoreRequestDto playerScoreRequestDto,
                                              @MappingTarget PlayerGameplayEntity playerGameplay) throws Exception;
    
    @Mapping(target = "id", ignore = true)
    CardGameplayEntity updateCardGameplay(CardScoreRequestDto cardScoreRequestDto,
                                          @MappingTarget CardGameplayEntity cardGameplay) throws Exception;
}