package com.tcc.app.web.memory_game.api.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor(force = true)
//@RequiredArgsConstructor
//@EqualsAndHashCode(exclude = {"id", "playerGameplaySet", "codeGameplay"})

@Entity
@Table(name = "gameplay")
@Data
@EqualsAndHashCode(exclude = {"id", "playerGameplaySet", "codeGameplay"})
public class GameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Integer numbersPlayer = 0;
    
    @NonNull
    @Column(nullable = false)
    private final Boolean alone;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id")
    private final MemoryGameEntity memoryGame;
    
    @Column(name = "start_time", nullable = false)
    private final LocalDateTime startTime = LocalDateTime.now();
    
    @OneToMany(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    @OneToOne(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodeGameplayEntity codeGameplay;
    
    public GameplayEntity sumOnePlayer() {
        ++this.numbersPlayer;
        return this;
    }
    
    public GameplayEntity addPlayerGameplay(@NonNull PlayerGameplayEntity playerGameplay) {
        //playerGameplay.setGameplay(this);
        playerGameplaySet.add(playerGameplay);
        return this;
    }
    
    public GameplayEntity updatePlayerGameplay(@NonNull PlayerGameplayEntity playerGameplay) {
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