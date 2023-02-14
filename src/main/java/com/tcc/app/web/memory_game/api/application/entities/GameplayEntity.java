package com.tcc.app.web.memory_game.api.application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_game_id")
    private MemoryGameEntity memoryGame;
    
    @OneToMany(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    @OneToOne(mappedBy = "gameplay", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodeGameplayEntity codeGameplay;
    
    public void sumOnePlayer() {
        ++this.numbersPlayer;
    }
    
    public void addPlayerGameplay(PlayerGameplayEntity playerGameplay) {
        playerGameplaySet.add(playerGameplay);
    }
    
    public void alterPlayerGameplay(PlayerGameplayEntity playerGameplay) {
        playerGameplaySet.remove(playerGameplay);
        playerGameplaySet.add(playerGameplay);
    }
}