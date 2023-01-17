package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardEntity toCardEntity(CardRequestDto cardRequestDto);
}