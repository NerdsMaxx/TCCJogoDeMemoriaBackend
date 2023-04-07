package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.entities.CardEntity;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CardMapper {
    @Mapping(source = "cardRequestDto.firstContent", target = "firstContent")
    @Mapping(source = "cardRequestDto.secondContent", target = "secondContent")
    @Mapping(source = "memoryGame", target = "memoryGame")
    @Mapping(source = "memoryGame.id", target = "id", ignore = true)
    CardEntity toCardEntity(CardRequestDto cardRequestDto, MemoryGameEntity memoryGame);

    CardRequestDto toCardRequestDto(CardEntity card);
}