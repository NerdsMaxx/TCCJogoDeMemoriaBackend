package com.tcc.jogodememoria.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "CARDS",
       uniqueConstraints = {
               @UniqueConstraint(
                       name = "unique_question_answer_memory_game",
                       columnNames = {"question", "answer", "memory_game_id"}
               )
       }
)
public class CardModel {
    
    public CardModel () {
    }
    
    public CardModel (String question, String answer, MemoryGameModel memoryGame) {
        this.question = question;
        this.answer = answer;
        this.memoryGame = memoryGame;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String question;
    
    @Column(nullable = false)
    private String answer;
    
    @ManyToOne
    @JoinColumn(name = "memory_game_id", nullable = false)
    private MemoryGameModel memoryGame;
}