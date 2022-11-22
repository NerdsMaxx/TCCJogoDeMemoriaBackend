package com.tcc.jogodememoria.backend.dtos.memorygame;

import com.tcc.jogodememoria.backend.dtos.card.CardDto;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Set;

@Getter
public class MemoryGameUpdateDto {
    
    @Nullable
    private String name;
    
    @Nullable
    private Set<String> subjects;
    
    @Nullable
    private Set<CardDto> cards;
    
}