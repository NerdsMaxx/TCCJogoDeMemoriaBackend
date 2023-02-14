package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
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
    private CreatorEntity creator;
    
    @ManyToMany
    @JoinTable(name = "player_memory_game",
               joinColumns = @JoinColumn(name = "memory_game_id"),
               inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<PlayerEntity> playerSet = new HashSet<>();
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "memory_game_subject", joinColumns = @JoinColumn(name = "memory_game_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<SubjectEntity> subjectSet = new HashSet<>();
    
    @OneToMany(mappedBy = "memoryGame", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardEntity> cardSet = new HashSet<>();
    
    @OneToMany(mappedBy = "memoryGame")
    private Set<GameplayEntity> gameplaySet = new HashSet<>();
    
    public void addPlayer(PlayerEntity player) {
        playerSet.add(player);
    }
    
    public void addCardListFromResquestDto(Set<CardRequestDto> cardRequestDtoList, CardMapper cardMapper) {
        Set<CardEntity> newCardSet = cardRequestDtoList.stream()
                                                    .map(cardMapper::toCardEntity)
                                                    .collect(Collectors.toSet());
        newCardSet.forEach(card -> card.setMemoryGame(this));
        this.cardSet = newCardSet;
    }
    
    public void clearCardSet() {
        cardSet.forEach(card -> card.setMemoryGame(null));
        cardSet.clear();
    }
    
    public void addSubjectListFromRequestDto(Set<String> subjectRequestDtoSet, Set<SubjectEntity> subjectFoundSet) {
        Set<SubjectEntity> newSubjectSet = new HashSet<>(subjectFoundSet);
        newSubjectSet.addAll(subjectRequestDtoSet.stream().map(SubjectEntity::new).collect(Collectors.toSet()));
        this.subjectSet = newSubjectSet;
    }
    
    public void clearSubjectSet() {
        subjectSet.forEach(subject -> subject.removeMemoryGame(this));
        subjectSet.clear();
    }
}