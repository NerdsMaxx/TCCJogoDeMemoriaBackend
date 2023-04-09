package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_gameplay")
//        , uniqueConstraints = {@UniqueConstraint(name = "unique_player_gameplay",
//                                              columnNames = {"player_id", "gameplay_id"})})

//@EqualsAndHashCode(exclude = {"id", "score", "numberPairCardCorrect", "numberPairCardWrong", "cardGameplaySet"})
@Data
//@EqualsAndHashCode(exclude = {"id", "score"})
public class PlayerGameplayEntity {
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    
    public PlayerGameplayEntity(UserEntity player, GameplayEntity gameplay) {
        this.player = player;
        this.gameplay = gameplay;
        this.id = new PlayerGameplayId(player.getId(), gameplay.getId());
    }
    
    @EmbeddedId
    private PlayerGameplayId id;
    
    @Column(nullable = false)
    private Integer score = 0;
    
//    @Column(name = "correct", nullable = false)
//    private Integer numberPairCardCorrect = 0;
//
//    @Column(name = "wrong", nullable = false)
//    private Integer numberPairCardWrong = 0;
    
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
    
//    @OneToMany(mappedBy = "playerGameplay", cascade = CascadeType.ALL)
//    private Set<CardGameplayEntity> cardGameplaySet = new HashSet<>();
    
//    public CardGameplayEntity findCardGameplay(CardScoreRequestDto cardScoreRequestDto) throws Exception {
//        return cardGameplaySet.stream().filter(cardGameplay -> cardGameplay.equalsCardId(cardScoreRequestDto.id()))
//                              .findFirst()
//                              .orElseThrow(() -> new EntityNotFoundException(
//                                      "Carta não encontrado pela informações fornecidas!"));
//    }
}