package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.dtos.responses.MemoryGameResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SubjectMapper.class}, componentModel = "spring")
public interface MemoryGameMapper {
    
    @Mapping(source = "memoryGame.memoryGame", target = "name")
    @Mapping(source = "memoryGame.creator.user.username", target = "user.username")
    @Mapping(source = "memoryGame.creator.user.name", target = "user.name")
    @Mapping(source = "memoryGame.creator.user.email", target = "user.email")
    @Mapping(source = "memoryGame.creator.user.userType.type", target = "user.type")
    MemoryGameResponseDto toMemoryGameResponseDto(MemoryGameEntity memoryGame);
}