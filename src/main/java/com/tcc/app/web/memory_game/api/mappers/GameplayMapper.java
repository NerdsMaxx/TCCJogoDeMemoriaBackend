package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.dtos.responses.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {MemoryGameMapper.class})
//@DecoratedWith(GameplayMapperDecorator.class)
public interface GameplayMapper {
    
    GameplayResponseDto toGameplayResponseDto(CodeGameplayEntity codeGameplay);
    
    
    @Mapping(source = "player.username", target = "player")
    @Mapping(source = "gameplay.memoryGame", target = "memoryGame")
    PlayerAddedResponseDto toPlayerAddedResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "player.username", target = "player")
    PlayerResultResponseDto toResultPlayerResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "playerGameplaySet", target = "playerSet")
    @Mapping(expression = "java(codeGameplay.getNumbersPlayerMoment() == 0)", target = "noPlayers")
    GameplayResultResponseDto toGameplayResultDto(Set<PlayerGameplayEntity> playerGameplaySet,
                                                  CodeGameplayEntity codeGameplay,
                                                  String memoryGame,
                                                  String creator);
    
    default CodesResponseDto toCodesResponseDto(Set<CodeGameplayEntity> codeGameplaySet) {
        Set<MemoryGameWithCodeResponseDto> codes = codeGameplaySet.stream()
                                                                  .map(code -> new MemoryGameWithCodeResponseDto(
                                                                          code.getGameplay().getMemoryGame()
                                                                              .getCreator().getUsername(),
                                                                          code.getGameplay().getMemoryGame().getMemoryGame(),
                                                                          code.getCode()))
                                                                  .collect(Collectors.toSet());
        
        
        return new CodesResponseDto(codes);
    }
    
    PlayerGameplayEntity updatePlayerGameplay(PlayerScoreRequestDto playerScoreRequestDto,
                                              @MappingTarget PlayerGameplayEntity playerGameplay) throws Exception;
    
    @Mapping(source = "gameplay.memoryGame.creator.username", target = "creator")
    @Mapping(source = "gameplay.memoryGame.memoryGame", target = "memoryGame")
    PreviousGameplaysResponseDto toPreviousGameplaysResponseDTO(PlayerGameplayEntity playerGameplay);
    
//    @Mapping(target = "id", ignore = true)
//    CardGameplayEntity updateCardGameplay(CardScoreRequestDto cardScoreRequestDto,
//                                          @MappingTarget CardGameplayEntity cardGameplay) throws Exception;
}