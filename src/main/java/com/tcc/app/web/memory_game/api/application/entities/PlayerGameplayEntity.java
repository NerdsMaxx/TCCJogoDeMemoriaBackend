package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardScoreRequestDto;
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
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "score", "numberCardCorrect", "numberCardWrong", "cardGameplaySet"})
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameplay_id", nullable = false)
    private GameplayEntity gameplay;
    @OneToMany(mappedBy = "playerGameplay", cascade = CascadeType.ALL)
    private Set<CardGameplayEntity> cardGameplaySet = new HashSet<>();
    
    public PlayerGameplayEntity(PlayerEntity player, GameplayEntity gameplay) {
        this.player = player;
        this.gameplay = gameplay;
        this.cardGameplaySet.addAll(gameplay.generateCardGameplaySet(this));
    }
    
    public CardGameplayEntity findCardGameplay(CardScoreRequestDto cardScoreRequestDto) throws Exception {
        return cardGameplaySet.stream().filter(cardGameplay -> cardGameplay.equalsCardId(cardScoreRequestDto.id()))
                              .findFirst()
                              .orElseThrow(() -> new EntityNotFoundException("Carta não encontrado pela informações fornecidas!"));
    }
}