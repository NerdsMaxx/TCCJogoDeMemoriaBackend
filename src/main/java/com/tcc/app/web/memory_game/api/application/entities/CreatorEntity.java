package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "creator")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @OneToMany(mappedBy = "creator")
    private Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
}