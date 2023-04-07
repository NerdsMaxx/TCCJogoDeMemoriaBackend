package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.CustomException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "memory_game",
       uniqueConstraints = {@UniqueConstraint(name = "unique_memory_game_creator",
                                              columnNames = {"memory_game", "creator_id"})}
)
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(exclude = {"id", "playerSet", "subjectSet", "cardSet", "gameplaySet"})
public class MemoryGameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @NotBlank
    @Column(name = "memory_game", nullable = false)
    private String memoryGame;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private final UserEntity creator;
    
//    @ManyToMany
//    @JoinTable(name = "player_memory_game",
//               joinColumns = @JoinColumn(name = "memory_game_id"),
//               inverseJoinColumns = @JoinColumn(name = "player_id"))
//    private final Set<UserEntity> playerSet = new HashSet<>();
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "memory_game_subject", joinColumns = @JoinColumn(name = "memory_game_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private final Set<SubjectEntity> subjectSet = new HashSet<>();
    
    @OneToMany(mappedBy = "memoryGame", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<CardEntity> cardSet = new HashSet<>();
    
    @OneToMany(mappedBy = "memoryGame")
    private final Set<GameplayEntity> gameplaySet = new HashSet<>();
    
//    public MemoryGameEntity addPlayer(UserEntity player) throws CustomException {
//        if(! player.isPlayer()) {
//            throw new CustomException("Este usuário não é jogador");
//        }
//
//        playerSet.add(player);
//        return this;
//    }
    
    public MemoryGameEntity addCardSet(@NonNull Set<CardEntity> newCardSet) {
        newCardSet.forEach(card -> card.setMemoryGame(this));
        cardSet.addAll(newCardSet);
        return this;
    }
    
    public MemoryGameEntity clearCardSet(@NonNull Set<CardEntity> cardSet) {
        this.cardSet.removeIf(card -> !cardSet.contains(card));
        return this;
    }
    
    public MemoryGameEntity clearCardSet() {
        cardSet.clear();
        return this;
    }
    
    public MemoryGameEntity addSubjectList(@NonNull Set<SubjectEntity> subjectSet,
                                           @NonNull Set<SubjectEntity> subjectFoundSet) {
        Set<SubjectEntity> newSubjectSet = new HashSet<>(subjectFoundSet);
        newSubjectSet.addAll(subjectSet);
        
        this.subjectSet.addAll(newSubjectSet);
        return this;
    }
    
    public MemoryGameEntity clearSubjectSet(@NonNull Set<String> subjectNameSet) {
        subjectSet.removeIf(subject -> !subjectNameSet.contains(subject.getSubject()));
        return this;
    }
    
    public MemoryGameEntity clearSubjectSet() {
        subjectSet.clear();
        return this;
    }
}