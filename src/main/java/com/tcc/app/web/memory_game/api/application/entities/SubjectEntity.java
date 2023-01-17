package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "subject")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private String subject;
    
    @ManyToMany
    @JoinTable( name = "user_subject", joinColumns = @JoinColumn( name = "subject_id" ),
                inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private List<UserEntity> userList = new LinkedList<>();
    
    @ManyToMany
    @JoinTable(name = "memory_game_subject", joinColumns = @JoinColumn(name = "subject_id"),
               inverseJoinColumns = @JoinColumn(name = "memory_game_id"))
    private List<MemoryGameEntity> memoryGameList = new LinkedList<>();
}