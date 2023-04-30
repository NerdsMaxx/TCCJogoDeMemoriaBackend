package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card",
       uniqueConstraints = {@UniqueConstraint(name = "unique_first_content_second_content_memory_game",
                                              columnNames = {"first_content", "second_content", "memory_game_id"})})

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = {"firstContent", "secondContent", "memoryGame"})

public class CardEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter Long id;
    
    
    @Column(nullable = false, name = "first_content")
    private final @NonNull String firstContent;
    
    
    @Column(nullable = false, name = "second_content")
    private final @NonNull String secondContent;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id", nullable = false)
    private final @NonNull MemoryGameEntity memoryGame;
}