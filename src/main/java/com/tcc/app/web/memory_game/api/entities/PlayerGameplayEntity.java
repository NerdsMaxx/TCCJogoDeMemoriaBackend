package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_gameplay")

@Getter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = {"id", "score", "startTime", "endTime"})

public class PlayerGameplayEntity {
    
    @Default
    public PlayerGameplayEntity(@NonNull UserEntity player, @NonNull GameplayEntity gameplay) {
        this.id = new PlayerGameplayId(player.getId(), gameplay.getId());
        this.player = player;
        this.gameplay = gameplay;
    }
    
    @EmbeddedId
    private final @NonNull PlayerGameplayId id;
    
    @Column(nullable = false)
    private @Setter @NonNull Integer score = 0;
    
    @Column(name = "start_time", nullable = false)
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "number_right_options")
    private @Setter @NonNull Integer numberRightOptions = 0;
    
    @Column(name = "number_wrong_options")
    private @Setter @NonNull Integer numberWrongOptions = 0;
    
    @Column(name = "number_attempts")
    private @Setter @NonNull Integer numberAttempts = 0;
   
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = false)
    private final @NonNull UserEntity player;
    
   
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameplayId")
    @JoinColumn(name = "gameplay_id", nullable = false)
    private final @NonNull GameplayEntity gameplay;
    
    public PlayerGameplayEntity finishGameplay() {
        endTime = LocalDateTime.now();
        return this;
    }
}