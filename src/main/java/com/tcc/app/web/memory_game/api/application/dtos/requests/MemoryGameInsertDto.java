package com.tcc.app.web.memory_game.api.application.dtos.requests;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record MemoryGameInsertDto(@NotBlank String name, Set<String> subjectSet,
		@NotEmpty Set<CardInsertDto> cardSet) {

}
