package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table( name = "memory_game",
        uniqueConstraints = { @UniqueConstraint( name = "unique_memory_game_user",
                                                 columnNames = { "memory_game", "user_id" } ) }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( of = "id" )
public class MemoryGameEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    @NotBlank
    @Column( name = "memory_game", nullable = false )
    private String memoryGame;
    
    @ManyToOne
    @JoinColumn( name = "user_id", nullable = false )
    private UserEntity user;
    
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "memory_game_subject", joinColumns = @JoinColumn( name = "memory_game_id" ),
                inverseJoinColumns = @JoinColumn( name = "subject_id" ) )
    private Set<SubjectEntity> subjectSet;
    
    @OneToMany( mappedBy = "memoryGame", fetch = FetchType.EAGER )
    private Set<CardEntity> cardSet;
    
    @OneToMany( mappedBy = "memoryGame", fetch = FetchType.EAGER )
    private Set<ScoreEntity> scoreSet;
}