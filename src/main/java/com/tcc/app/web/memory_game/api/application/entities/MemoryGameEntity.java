package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.application.entities.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "memory_game",
       uniqueConstraints = {@UniqueConstraint(name = "unique_memory_game_creator",
                                              columnNames = {"memory_game", "creator_id"})}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class MemoryGameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "memory_game", nullable = false)
    private String memoryGame;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;
    
    @ManyToMany(mappedBy = "memoryGameList")
    private List<PlayerEntity> playerList = new LinkedList<>();
    
    @ManyToMany(mappedBy = "memoryGameList")
    private List<SubjectEntity> subjectList = new LinkedList<>();
    
    @OneToMany(mappedBy = "memoryGame")
    private List<CardEntity> cardList = new LinkedList<>();
    
    @OneToMany(mappedBy = "memoryGame")
    private List<GameplayEntity> gameplayList = new LinkedList<>();
}