package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.*;
import com.tcc.app.web.memory_game.api.application.entities.CardGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {MemoryGameMapper.class})
@DecoratedWith(GameplayMapperDecorator.class)
public interface GameplayMapper {
    
    GameplayResponseDto toGameplayResponseDto(CodeGameplayEntity codeGameplay);
    
    
    @Mapping(source = "player.user.username", target = "player")
    @Mapping(source = "gameplay.memoryGame", target = "memoryGame")
    PlayerAddedResponseDto toPlayerAddedResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "player.user.username", target = "player")
    PlayerResultResponseDto toResultPlayerResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "playerGameplaySet", target = "playerSet")
    @Mapping(expression = "java(codeGameplay.getNumbersPlayerMoment() == 0)", target = "allFinished")
    GameplayResultResponseDto toGameplayResultDto(Set<PlayerGameplayEntity> playerGameplaySet,
                                                  CodeGameplayEntity codeGameplay,
                                                  String memoryGame,
                                                  String creator);
    
    default CodesResponseDto toCodesResponseDto(Set<CodeGameplayEntity> codeGameplaySet) {
        Set<MemoryGameWithCodeResponseDto> codes = codeGameplaySet.stream()
                                                                  .map(code -> new MemoryGameWithCodeResponseDto(
                                                                          code.getGameplay().getMemoryGame()
                                                                              .getCreator().getUser().getUsername(),
                                                                          code.getGameplay().getMemoryGame().getMemoryGame(),
                                                                          code.getCode())
                                                                  )
                                                                  .collect(Collectors.toSet());
        
        
        return new CodesResponseDto(codes);
    }
    
    PlayerGameplayEntity updatePlayerGameplay(PlayerScoreRequestDto playerScoreRequestDto,
                                              @MappingTarget PlayerGameplayEntity playerGameplay) throws Exception;
    
    @Mapping(target = "id", ignore = true)
    CardGameplayEntity updateCardGameplay(CardScoreRequestDto cardScoreRequestDto,
                                          @MappingTarget CardGameplayEntity cardGameplay) throws Exception;
}