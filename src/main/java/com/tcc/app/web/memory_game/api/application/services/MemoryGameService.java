package com.tcc.app.web.memory_game.api.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameInsertDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;

@Service
public class MemoryGameService {

		@Autowired
		private SubjectService subjectService;

		@Autowired
		private CardService cardService;
		
		@Autowired
		private UserService userService;

		@Autowired
		private MemoryGameRepository memoryGameRepository;

		public MemoryGameEntity registerNewMemoryGame( MemoryGameInsertDto memoryGameInsertDto ) {
				UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				var memoryGame = new MemoryGameEntity();
				memoryGame.setName( memoryGameInsertDto.name() );
				memoryGame.setUser( user );

				//memoryGame = memoryGameRepository.save( memoryGame );

				var cardSet = cardService.registerNewCards( memoryGameInsertDto.cardSet(), memoryGame );
				var subjectSet = subjectService.registerNewSubjects( memoryGameInsertDto.subjectSet(), memoryGame, user );
				
				memoryGame.setCardSet( cardSet );
				memoryGame.setSubjectSet( subjectSet );
				userService.addSubjectSet( subjectSet );

				return memoryGameRepository.save( memoryGame );
		}
}
