package com.tcc.app.web.memory_game.api.infrastructures.security.controllers;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.LoginResponseDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.AuthenticationDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserTypeEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDto.username(),
                                                                          authenticationDto.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String jwtToken = tokenService.generateToken(user);
        
        return ResponseEntity.ok(new LoginResponseDto(user.getUsername(),
                                                      user.getEmail(),
                                                      user.getUserType().stream()
                                                          .map(userType -> userType.getType().toString())
                                                          .collect(Collectors.toSet()),
                                                      jwtToken));
        
    }
}