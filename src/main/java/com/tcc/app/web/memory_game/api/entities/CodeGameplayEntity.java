package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_gameplay")
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"code"})
public class CodeGameplayEntity {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private final String code;
    
    @Column(nullable = false)
    private Integer numbersPlayerMoment = 0;
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "gameplay_id", nullable = false, unique = true)
    private final GameplayEntity gameplay;
    
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