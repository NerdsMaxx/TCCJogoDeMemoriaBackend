package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.entities.CardEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CardMapper {
    CardEntity toCardEntity(CardRequestDto cardRequestDto);

    CardRequestDto toCardRequestDto(CardEntity card);
}