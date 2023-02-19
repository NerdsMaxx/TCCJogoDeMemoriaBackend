package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.dtos.responses.MemoryGameResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SubjectMapper.class, CardMapper.class})
public interface MemoryGameMapper {
    
    @Mapping(source = "memoryGame", target = "name")
    @Mapping(source = "creator.user.username", target = "creator")
    MemoryGameResponseDto toMemoryGameResponseDto(MemoryGameEntity memoryGame);
}