package com.tcc.app.web.memory_game.api.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameInsertDto;
import com.tcc.app.web.memory_game.api.application.mappers.MemoryGameMapper;
import com.tcc.app.web.memory_game.api.application.services.MemoryGameService;
import jakarta.validation.Valid;

@RestController
@RequestMapping( "/jogo-de-memoria" )
@CrossOrigin( "*" )
public class MemoryGameController {

		@Autowired
		private MemoryGameService memoryGameService;

		@Autowired
		private MemoryGameMapper memoryGameMapper;

		@Transactional
		@PostMapping
		public ResponseEntity insertNewMemoryGame( @RequestBody @Valid MemoryGameInsertDto memoryGameInsertDto,
						UriComponentsBuilder uriBuilder ) {
				var memoryGame = memoryGameService.registerNewMemoryGame( memoryGameInsertDto );

				var uri = uriBuilder.path( "/jogo-de-memoria/{id}" ).buildAndExpand( memoryGame.getId() ).toUri();

				return ResponseEntity.created( uri ).body( memoryGameMapper.convertEntityToDetailsDto( memoryGame ) );
		}
}
