package com.tcc.app.web.memory_game.api.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameInsertDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;

@Service
public class MemoryGameService {

		@Autowired
		private UserService userService;

		@Autowired
		private SubjectService subjectService;

		@Autowired
		private CardService cardService;

		@Autowired
		private MemoryGameRepository memoryGameRepository;

		@Transactional
		public MemoryGameEntity registerNewMemoryGame( MemoryGameInsertDto memoryGameInsertDto ) {
				UserEntity user = userService.getCurrentUser();

				var memoryGame = new MemoryGameEntity();
				memoryGame.setMemoryGame( memoryGameInsertDto.name() );
				memoryGame.setUser( user );
				
				memoryGame = memoryGameRepository.save( memoryGame );
				
				var cardSet = cardService.registerNewCardsForMemoryGame( memoryGameInsertDto.cardSet(), memoryGame );
				var subjectSet = subjectService.registerNewSubjectsForMemoryGame( memoryGameInsertDto.subjectSet(), memoryGame, user );

				memoryGame.setCardSet( cardSet );
				memoryGame.setSubjectSet( subjectSet );

				return  memoryGame;
		}
}