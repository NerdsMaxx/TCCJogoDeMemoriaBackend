package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "gameplay")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = {"alone", "memoryGame", "usedCode", "startTime", "lastTime", "numbersPlayer"})

public class GameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter Long id;
    
    @Column(nullable = false)
    private final @NonNull Boolean alone;
    
    @Column(name = "start_time", nullable = false)
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @Column(name = "numbers_player", nullable = false)
    private Integer numbersPlayer = 0;
    
    @Column(name = "used_code")
    private @NonNull String usedCode;
    
    @Column(name = "last_time")
    private LocalDateTime lastTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id")
    private final @NonNull MemoryGameEntity memoryGame;
    
    @OneToOne(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodeGameplayEntity codeGameplay;
    
    @OneToMany(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    public GameplayEntity setCodeGameplay(CodeGameplayEntity codeGameplay) {
        this.codeGameplay = codeGameplay;
        return this;
    }
    
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
        
        final LocalDateTime endTime = playerGameplay.getEndTime();
        if(lastTime == null || endTime.isAfter(lastTime)) {
            lastTime = endTime;
        }
        
        return this;
    }
}