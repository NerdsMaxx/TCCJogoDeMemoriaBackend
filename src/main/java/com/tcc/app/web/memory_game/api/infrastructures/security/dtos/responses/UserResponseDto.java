package com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses;

public record UserResponseDto(Long id,
                              String name,
                              String username,
                              String email,
                              String type ) {

}