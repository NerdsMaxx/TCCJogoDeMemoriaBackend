package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_gameplay")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class CodeGameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private Integer numbersPlayerMoment = 0;
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "gameplay_id", nullable = false, unique = true)
    GameplayEntity gameplay;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime = LocalDateTime.now().plusHours(2);
    
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