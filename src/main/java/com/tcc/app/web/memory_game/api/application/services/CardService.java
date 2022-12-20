package com.tcc.app.web.memory_game.api.application.services;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcc.app.web.memory_game.api.application.dtos.requests.CardInsertDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CardRepository;

@Service
public class CardService {

		@Autowired
		private CardRepository cardRepository;

		@Autowired
		private CardMapper cardMapper;

		public Set<CardEntity> registerNewCards( Set<CardInsertDto> cardInsertDtoSet, MemoryGameEntity memoryGame ) {
				var cardSet = new HashSet<CardEntity>();

				for ( var cardInsertDto : cardInsertDtoSet ) {
						var card = cardMapper.convertInsertDtoToEntity( cardInsertDto );
						card.setMemoryGame( memoryGame );

						card = cardRepository.save( card );
						cardSet.add( card );
				}

				return cardSet;
		}
}
