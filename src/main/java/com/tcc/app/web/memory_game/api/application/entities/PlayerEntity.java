package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.application.utils.ListUtilStatic;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "player")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class PlayerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
    
    @ManyToMany
    @JoinTable(name = "player_memory_game",
               joinColumns = @JoinColumn(name = "player_id"),
               inverseJoinColumns = @JoinColumn(name = "memory_game_id"))
    private List<MemoryGameEntity> memoryGameList = new LinkedList<>();
    
    @OneToMany(mappedBy = "player")
    private List<PlayerGameplayEntity> playerGameplayList = new LinkedList<>();
    
    public PlayerEntity addMemoryGame(MemoryGameEntity memoryGame) {
        ListUtilStatic.addElementIfNotExist(memoryGame, memoryGameList);
        return this;
    }
    
}