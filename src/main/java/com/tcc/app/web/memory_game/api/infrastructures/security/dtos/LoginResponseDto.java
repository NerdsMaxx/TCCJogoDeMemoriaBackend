package com.tcc.app.web.memory_game.api.infrastructures.security.dtos;

public record LoginResponseDto(String username,
                                String email,
                                String type,
                                String jwtToken) {

}