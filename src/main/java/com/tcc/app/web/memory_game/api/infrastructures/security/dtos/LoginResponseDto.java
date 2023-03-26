package com.tcc.app.web.memory_game.api.infrastructures.security.dtos;

import java.util.Set;

public record LoginResponseDto(String username,
                               String email,
                               Set<String> type,
                               String jwtToken) {

}