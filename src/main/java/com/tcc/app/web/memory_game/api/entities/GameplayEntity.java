package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gameplay")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = {"alone", "memoryGame", "startTime", "numbersPlayer"})

public class GameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter Long id;
    
    @Column(nullable = false)
    private final @NonNull Boolean alone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id")
    private final @NonNull MemoryGameEntity memoryGame;
    
    @Column(name = "start_time", nullable = false)
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @Column
    private Integer numbersPlayer = 0;
    
    @OneToOne(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private @Setter CodeGameplayEntity codeGameplay;
    
    @OneToMany(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
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