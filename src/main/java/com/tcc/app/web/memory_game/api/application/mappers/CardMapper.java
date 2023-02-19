package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.CardScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.CardGameplayEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CardMapper {
    CardEntity toCardEntity(CardRequestDto cardRequestDto);
    
    CardRequestDto toCardRequestDto(CardEntity card);
}