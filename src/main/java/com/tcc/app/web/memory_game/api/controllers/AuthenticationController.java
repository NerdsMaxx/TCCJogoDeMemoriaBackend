package com.tcc.app.web.memory_game.api.controllers;

import com.tcc.app.web.memory_game.api.dtos.requests.AuthenticationDto;
import com.tcc.app.web.memory_game.api.dtos.responses.LoginResponseDto;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.services.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    
    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDto.username(),
                                                                                authenticationDto.password());
        
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        final UserEntity user = (UserEntity) authentication.getPrincipal();
        final String jwtToken = tokenService.generateToken(user);
        
        return ResponseEntity.ok(new LoginResponseDto(user.getUsername(),
                                                      user.getEmail(),
                                                      user.getUserType().stream()
                                                          .map(userType -> userType.getType().toString())
                                                          .collect(Collectors.toSet()),
                                                      jwtToken));
        
    }
}