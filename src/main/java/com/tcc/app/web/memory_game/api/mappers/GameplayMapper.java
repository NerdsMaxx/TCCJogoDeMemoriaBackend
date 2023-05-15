package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.dtos.responses.*;
import com.tcc.app.web.memory_game.api.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.entities.PlayerGameplayEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {MemoryGameMapper.class})
public interface GameplayMapper {
    
    GameplayResponseDto toGameplayResponseDto(CodeGameplayEntity codeGameplay);
    
    @Mapping(source = "player.username", target = "player")
    @Mapping(source = "gameplay.memoryGame", target = "memoryGame")
    PlayerAddedResponseDto toPlayerAddedResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "player.username", target = "player")
    @Mapping(source = "gameplay.memoryGame.creator.username", target = "creator")
    PlayerResultResponseDto toResultPlayerResponseDto(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "playerGameplaySet", target = "playerSet")
    @Mapping(expression = "java(codeGameplay.getNumbersPlayerMoment() == 0)", target = "noPlayers")
    GameplayResultResponseDto toGameplayResultDto(Set<PlayerGameplayEntity> playerGameplaySet,
                                                  CodeGameplayEntity codeGameplay,
                                                  String memoryGame,
                                                  String creator);
    
    default CodesResponseDto toCodesResponseDto(List<CodeGameplayEntity> codeGameplaySet) {
        final Set<MemoryGameWithCodeResponseDto> codes;
        codes = codeGameplaySet.stream()
                               .map(code -> {
                                   final MemoryGameEntity memoryGame = code.getGameplay().getMemoryGame();
                                   
                                   return new MemoryGameWithCodeResponseDto(memoryGame.getCreator().getUsername(),
                                                                            memoryGame.getMemoryGame(),
                                                                            code.getCode());
                               })
                               .collect(Collectors.toSet());
        
        return new CodesResponseDto(codes);
    }
    
    PlayerGameplayEntity updatePlayerGameplay(PlayerScoreRequestDto playerScoreRequestDto,
                                              @MappingTarget PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "gameplay.memoryGame.creator.username", target = "creator")
    @Mapping(source = "gameplay.memoryGame.memoryGame", target = "memoryGame")
    @Mapping(source = "player.username", target = "player")
    PreviousGameplaysPlayerResponseDto toPreviousGameplaysPlayerResponseDTO(PlayerGameplayEntity playerGameplay);
    
    @Mapping(source = "id", target = "gameplayId")
    @Mapping(source = "memoryGame.memoryGame", target = "memoryGame")
    PreviousGameplaysCreatorResponseDto toPreviousGameplaysCreatorResponseDto(GameplayEntity gameplay);
}