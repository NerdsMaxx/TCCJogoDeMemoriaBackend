package com.tcc.app.web.memory_game.api.application.mappers;

import org.springframework.stereotype.Component;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardInsertDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.CardDetailsDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.interfaces.MapperEntityToDetailsDtoInterface;
import com.tcc.app.web.memory_game.api.application.interfaces.MapperInsertDtoToEntityInterface;

@Component
public class CardMapper implements MapperInsertDtoToEntityInterface<CardInsertDto, CardEntity>,
				MapperEntityToDetailsDtoInterface<CardEntity, CardDetailsDto> {

		@Override
		public CardEntity convertInsertDtoToEntity( CardInsertDto cardInsertDto ) {
				var cardEntity = new CardEntity();

				cardEntity.setFirstContent( cardInsertDto.firstContent() );
				cardEntity.setSecondContent( cardInsertDto.secondContent() );

				return cardEntity;
		}

		@Override
		public CardDetailsDto convertEntityToDetailsDto( CardEntity cardEntity ) {
				return new CardDetailsDto( cardEntity.getId(), cardEntity.getFirstContent(), cardEntity.getSecondContent() );
		}

}
