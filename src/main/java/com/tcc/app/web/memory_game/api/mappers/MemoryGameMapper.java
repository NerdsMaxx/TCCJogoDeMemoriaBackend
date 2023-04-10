package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.dtos.responses.MemoryGameCardsResponseDto;
import com.tcc.app.web.memory_game.api.dtos.responses.MemoryGameResponseDto;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SubjectMapper.class, CardMapper.class})
public interface MemoryGameMapper {
    
    @Mapping(source = "memoryGame", target = "name")
    @Mapping(source = "creator.username", target = "creator")
    MemoryGameResponseDto toMemoryGameResponseDto(MemoryGameEntity memoryGame);
    
    @Mapping(source = "memoryGame", target = "name")
    @Mapping(source = "creator.username", target = "creator")
    MemoryGameCardsResponseDto toMemoryGameCardsResponseDto(MemoryGameEntity memoryGame);
}