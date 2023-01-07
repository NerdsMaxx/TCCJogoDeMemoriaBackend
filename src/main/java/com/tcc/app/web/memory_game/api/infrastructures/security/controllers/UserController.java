package com.tcc.app.web.memory_game.api.infrastructures.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserInsertDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping( "/usuario" )
@CrossOrigin( "*" )
public class UserController {

		@Autowired
		private UserService userService;
		
		@Autowired
		private UserMapper userMapper;

		@PostMapping
		public ResponseEntity insertNewUser( @RequestBody @Valid UserInsertDto userInsertDto,
						UriComponentsBuilder uriBuilder ) throws Exception {
				var user = userService.registerNewUser( userInsertDto );

				var uri = uriBuilder.path( "/usuario/{id}" ).buildAndExpand( user.getId() ).toUri();

				return ResponseEntity.created( uri ).body( userMapper.convertEntityToDetailsDto( user ) );
		}
}
