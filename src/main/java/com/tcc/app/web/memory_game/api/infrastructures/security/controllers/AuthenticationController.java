package com.tcc.app.web.memory_game.api.infrastructures.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.JwtTokenDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.AuthenticationDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
		
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private TokenService tokenService;
		
		@PostMapping
		public ResponseEntity login(@RequestBody @Valid AuthenticationDto authenticationDto) {
				var authenticationToken = new UsernamePasswordAuthenticationToken( authenticationDto.username(), authenticationDto.password() );
				var authentication = authenticationManager.authenticate( authenticationToken );
				
				var jwtToken = tokenService.generateToken( (UserEntity) authentication.getPrincipal() );
				
				return ResponseEntity.ok( new JwtTokenDto( jwtToken ));
				
		}
}
