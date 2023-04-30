package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_gameplay")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = {"code"})

public class CodeGameplayEntity {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter Long id;
    
    @Column(nullable = false, unique = true)
    private final @NonNull String code;
    
    @Column(nullable = false)
    private Integer numbersPlayerMoment = 0;
    
    @OneToOne
    @JoinColumn(name = "gameplay_id", nullable = false, unique = true)
    private final @NonNull GameplayEntity gameplay;
    
    @Column(name = "end_time", nullable = false)
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(2);
    
    public CodeGameplayEntity sumOnePlayer() {
        ++ numbersPlayerMoment;
        return this;
    }
    
    public CodeGameplayEntity removeOnePlayer() {
        if (numbersPlayerMoment > 0) {
            -- numbersPlayerMoment;
        }
        return this;
    }
}