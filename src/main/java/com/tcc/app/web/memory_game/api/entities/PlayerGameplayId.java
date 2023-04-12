package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode

public class PlayerGameplayId implements Serializable {
    
    @NonNull
    @Column(name = "player_id")
    private final Long playerId;
    
    @NonNull
    @Column(name = "gameplay_id")
    private final Long gameplayId;
}