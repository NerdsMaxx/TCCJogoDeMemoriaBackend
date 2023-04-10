package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gameplay")
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"alone", "memoryGame", "startTime", "numbersPlayer"})
public class GameplayEntity {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false)
    private final Boolean alone;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id")
    private final MemoryGameEntity memoryGame;
    
    @Column(name = "start_time", nullable = false)
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @OneToMany(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    @Column
    private Integer numbersPlayer = 0;
    
    @Setter
    @OneToOne(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodeGameplayEntity codeGameplay;
    
    public GameplayEntity sumOnePlayer() {
        ++ this.numbersPlayer;
        return this;
    }
    
    public GameplayEntity addPlayerGameplay(@NonNull PlayerGameplayEntity playerGameplay) {
        playerGameplaySet.add(playerGameplay);
        return this;
    }
    
    public GameplayEntity updatePlayerGameplay(@NonNull PlayerGameplayEntity playerGameplay) {
        playerGameplaySet.removeIf(playerGameplay1 -> playerGameplay1.equals(playerGameplay));
        playerGameplaySet.add(playerGameplay);
        return this;
    }
}