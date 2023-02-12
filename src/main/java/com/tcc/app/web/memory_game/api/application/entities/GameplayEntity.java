package com.tcc.app.web.memory_game.api.application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "gameplay")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
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
    
    @OneToMany(mappedBy = "gameplay")
    private List<PlayerGameplayEntity> playerGameplayList = new LinkedList<>();
    
    public void sumOnePlayer() {
        ++this.numbersPlayer;
    }
}