package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_gameplay",
       uniqueConstraints = {@UniqueConstraint(name = "unique_player_gameplay",
                                              columnNames = {"player_id", "gameplay_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(exclude = {"id", "score", "numberPairCardCorrect", "numberPairCardWrong", "cardGameplaySet"})
@EqualsAndHashCode(exclude = {"id", "score"})
public class PlayerGameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer score = 0;
    
//    @Column(name = "correct", nullable = false)
//    private Integer numberPairCardCorrect = 0;
//
//    @Column(name = "wrong", nullable = false)
//    private Integer numberPairCardWrong = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private UserEntity player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameplay_id", nullable = false)
    private GameplayEntity gameplay;
    
//    @OneToMany(mappedBy = "playerGameplay", cascade = CascadeType.ALL)
//    private Set<CardGameplayEntity> cardGameplaySet = new HashSet<>();
    
    public PlayerGameplayEntity(UserEntity player, GameplayEntity gameplay) {
        this.player = player;
        this.gameplay = gameplay;
        //this.cardGameplaySet.addAll(gameplay.generateCardGameplaySet(this));
    }
    
//    public CardGameplayEntity findCardGameplay(CardScoreRequestDto cardScoreRequestDto) throws Exception {
//        return cardGameplaySet.stream().filter(cardGameplay -> cardGameplay.equalsCardId(cardScoreRequestDto.id()))
//                              .findFirst()
//                              .orElseThrow(() -> new EntityNotFoundException(
//                                      "Carta não encontrado pela informações fornecidas!"));
//    }
}