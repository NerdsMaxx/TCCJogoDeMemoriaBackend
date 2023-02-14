package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "player_gameplay",
       uniqueConstraints = {@UniqueConstraint(name = "unique_player_gameplay",
                                              columnNames = {"player_id", "gameplay_id"})})
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "cardGameplaySet"})
public class PlayerGameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer score = 0;
    
    @Column(name = "correct", nullable = false)
    private Integer numberCardCorrect = 0;
    
    @Column(name = "wrong", nullable = false)
    private Integer numberCardWrong = 0;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameplay_id", nullable = false)
    private GameplayEntity gameplay;
    
    @OneToMany(mappedBy = "playerGameplay")
    private Set<CardGameplayEntity> cardGameplaySet = new HashSet<>();
    
    public void setScores(PlayerScoreRequestDto playerScoreRequestDto) {
        this.score = playerScoreRequestDto.score();
        this.numberCardCorrect = playerScoreRequestDto.numberCardCorrect();
        this.numberCardWrong = playerScoreRequestDto.numberCardWrong();
    }
}