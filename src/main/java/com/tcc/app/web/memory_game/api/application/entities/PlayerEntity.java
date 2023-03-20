package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "player")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id", "memoryGameSet", "playerGameplaySet"})
public class PlayerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
    
    @ManyToMany(mappedBy = "playerSet")
    private Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
    
    @OneToMany(mappedBy = "player")
    private Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    public PlayerEntity addMemoryGame(MemoryGameEntity memoryGame) {
        memoryGameSet.add(memoryGame);
        return this;
    }
}