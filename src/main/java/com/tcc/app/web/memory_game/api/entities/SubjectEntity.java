package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subject")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = {"subject"})

public class SubjectEntity {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private final String subject;
    
    @ManyToMany(mappedBy = "subjectSet")
    private final Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
    
    public SubjectEntity removeMemoryGame(MemoryGameEntity memoryGame) {
        memoryGameSet.remove(memoryGame);
        return this;
    }
    
    public boolean noMemoryGame() {
        return memoryGameSet.isEmpty();
    }
    
    public boolean hasMemoryGame() {
        return ! noMemoryGame();
    }
}