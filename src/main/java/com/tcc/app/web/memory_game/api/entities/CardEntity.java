package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "card",
       uniqueConstraints = {@UniqueConstraint(name = "unique_first_content_second_content_memory_game",
                                              columnNames = {"first_content", "second_content", "memory_game_id"})})
@Data
@EqualsAndHashCode(exclude = {"id"})
public class CardEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, name = "first_content")
    private final String firstContent;
    
    @Column(nullable = false, name = "second_content")
    private final String secondContent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id", nullable = false)
    private final MemoryGameEntity memoryGame;
    
}