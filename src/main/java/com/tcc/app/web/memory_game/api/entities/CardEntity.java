package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "card",
       uniqueConstraints = {@UniqueConstraint(name = "unique_first_content_second_content_memory_game",
                                              columnNames = {"first_content", "second_content", "memory_game_id"})})
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"firstContent", "secondContent", "memoryGame"})
public class CardEntity {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false, name = "first_content")
    private final String firstContent;
    
    @NonNull
    @Column(nullable = false, name = "second_content")
    private final String secondContent;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id", nullable = false)
    private final MemoryGameEntity memoryGame;
    
}