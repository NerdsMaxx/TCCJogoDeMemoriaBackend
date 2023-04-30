package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "memory_game",
       uniqueConstraints = {@UniqueConstraint(name = "unique_memory_game_creator",
                                              columnNames = {"memory_game", "creator_id"})}
)

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = {"memoryGame", "creator"})

public class MemoryGameEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter Long id;
    
    @Column(name = "memory_game", nullable = false)
    private @Setter @NonNull String memoryGame;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private final @NonNull UserEntity creator;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "memory_game_subject", joinColumns = @JoinColumn(name = "memory_game_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private final Set<SubjectEntity> subjectSet = new HashSet<>();
    
    @OneToMany(mappedBy = "memoryGame", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<CardEntity> cardSet = new HashSet<>();
    
    @OneToMany(mappedBy = "memoryGame")
    private final Set<GameplayEntity> gameplaySet = new HashSet<>();
    
    public MemoryGameEntity addCardSet(@NonNull Set<CardEntity> newCardSet) {
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
    
    public MemoryGameEntity addSubjectSet(@NonNull Set<SubjectEntity> subjectSet,
                                          @NonNull Set<SubjectEntity> subjectFoundSet) {
        Set<SubjectEntity> newSubjectSet = new HashSet<>(subjectFoundSet);
        newSubjectSet.addAll(subjectSet);
        
        this.subjectSet.addAll(newSubjectSet);
        return this;
    }
    
    public MemoryGameEntity clearSubjectSet(@NonNull Set<String> subjectNameSet) {
        subjectSet.removeIf(subject -> ! subjectNameSet.contains(subject.getSubject()));
        return this;
    }
    
    public MemoryGameEntity clearSubjectSet() {
        subjectSet.clear();
        return this;
    }
    
    public Set<SubjectEntity> getSubjectSetNotUsed(@NonNull Set<String> subjectNameSet) {
        return subjectSet.stream().filter(subject -> ! subjectNameSet.contains(subject.getSubject()))
                         .collect(Collectors.toSet());
    }
}