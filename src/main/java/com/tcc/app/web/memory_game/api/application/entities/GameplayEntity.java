package com.tcc.app.web.memory_game.api.application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "gameplay")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id", "playerGameplaySet", "codeGameplay"})
public class GameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Integer numbersPlayer = 0;
    
    @Column(nullable = false)
    private Boolean alone = false;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id")
    private MemoryGameEntity memoryGame;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();
    
    @OneToMany(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    @OneToOne(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodeGameplayEntity codeGameplay;
    
    public GameplayEntity sumOnePlayer() {
        ++this.numbersPlayer;
        return this;
    }
    
    public GameplayEntity addPlayerGameplay(PlayerGameplayEntity playerGameplay) {
        playerGameplay.setGameplay(this);
        playerGameplaySet.add(playerGameplay);
        return this;
    }
    
    public GameplayEntity updatePlayerGameplay(PlayerGameplayEntity playerGameplay) {
        playerGameplaySet.removeIf(playerGameplay1 -> playerGameplay1.equals(playerGameplay));
        playerGameplaySet.add(playerGameplay);
        return this;
    }
    
//    public Set<CardGameplayEntity> generateCardGameplaySet(PlayerGameplayEntity playerGameplay) {
//        return memoryGame.getCardSet().stream()
//                         .map(card -> new CardGameplayEntity(playerGameplay, card))
//                         .collect(Collectors.toSet());
//    }
}