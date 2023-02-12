package com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses;

public record UserResponseDto(String name,
                              String username,
                              String email,
                              String type ) {

}