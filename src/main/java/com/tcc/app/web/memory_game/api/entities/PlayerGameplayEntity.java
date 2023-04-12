package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_gameplay")

@Getter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id", "score", "startTime", "endTime"})

public class PlayerGameplayEntity {
    
    public PlayerGameplayEntity(@NonNull UserEntity player, @NonNull GameplayEntity gameplay) {
        this.id = new PlayerGameplayId(player.getId(), gameplay.getId());
        this.player = player;
        this.gameplay = gameplay;
    }
    
    @NonNull
    @EmbeddedId
    private final PlayerGameplayId id;
    
    @Setter
    @Column(nullable = false)
    private Integer score = 0;
    
    @Column(name = "start_time", nullable = false)
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = false)
    private final UserEntity player;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameplayId")
    @JoinColumn(name = "gameplay_id", nullable = false)
    private final GameplayEntity gameplay;
    
    public PlayerGameplayEntity finishGameplay() {
        endTime = LocalDateTime.now();
        return this;
    }
}