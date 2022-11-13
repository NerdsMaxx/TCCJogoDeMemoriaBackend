package com.tcc.jogodememoria.backend.dtos;

import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
public class MemoryGameDto {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @Nullable
    private Set<String> subjects;

    @NotEmpty
    private Set<CardDto> cards;
}
