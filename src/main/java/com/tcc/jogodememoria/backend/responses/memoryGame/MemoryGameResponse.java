package com.tcc.jogodememoria.backend.responses.memoryGame;

import com.tcc.jogodememoria.backend.responses.card.CardResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MemoryGameResponse {
    
    private String username;
    
    private String name;
    
    private Set<String> subjects;
    
    private Set<CardResponse> cards;
    
}
