package com.tcc.app.web.memory_game.api.infrastructures.security.controllers;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.ResetPasswordRequestDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserRequestDto;
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
		public ResponseEntity insertNewUser( @RequestBody @Valid UserRequestDto userRequestDto,
						UriComponentsBuilder uriBuilder ) throws Exception {
				var user = userService.saveUser(userRequestDto);

				var uri = uriBuilder.path( "/usuario/{id}" ).buildAndExpand( user.getId() ).toUri();

				return ResponseEntity.created( uri ).body( userMapper.toUserResponseDto( user ) );
		}
		
		@PutMapping("/mudar-senha")
		public ResponseEntity changePassword(@RequestBody @Valid ResetPasswordRequestDto changePasswordRequestDto) {
			UserEntity user = userService.changePassword(changePasswordRequestDto);
			
			return ResponseEntity.ok(userMapper.toUserResponseDto(user));
		}
}