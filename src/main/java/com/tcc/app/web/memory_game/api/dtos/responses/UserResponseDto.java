package com.tcc.app.web.memory_game.api.dtos.responses;

import java.util.Set;

public record UserResponseDto(String name,
                              String username,
                              String email,
                              Set<String> type ) {

}