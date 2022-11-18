package com.tcc.jogodememoria.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "SCORES")
public class ScoreModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private Integer score;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
    
    @ManyToOne
    @JoinColumn(name = "memory_game_id", nullable = false)
    private MemoryGameModel memoryGame;
}