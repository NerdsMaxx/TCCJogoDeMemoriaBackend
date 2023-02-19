package com.tcc.app.web.memory_game.api.application.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subject")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id", "memoryGameSet"})
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private String subject;
    
    @ManyToMany(mappedBy = "subjectSet")
    private Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
    
    public SubjectEntity removeMemoryGame(MemoryGameEntity memoryGame) {
        memoryGameSet.remove(memoryGame);
        return this;
    }
}