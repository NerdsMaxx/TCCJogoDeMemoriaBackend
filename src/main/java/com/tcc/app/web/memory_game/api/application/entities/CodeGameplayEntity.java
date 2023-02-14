package com.tcc.app.web.memory_game.api.application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "code_gameplay")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
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
    
    private Date startDate = new Date();
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "gameplay_id", nullable = false)
    GameplayEntity gameplay;
    
    public void sumOnePlayer() {
        ++numbersPlayerMoment;
    }
    
    public void removeOnePlayer() {
        if(numbersPlayerMoment <= 0) {
            return;
        }
        
        --numbersPlayerMoment;
    }
}