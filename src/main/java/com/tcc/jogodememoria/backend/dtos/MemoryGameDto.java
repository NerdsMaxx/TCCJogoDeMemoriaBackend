package com.tcc.jogodememoria.backend.dtos;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
public class MemoryGameDto {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private Set<String> subjects;

    @NotEmpty
    private Set<CardDto> cards;
}
