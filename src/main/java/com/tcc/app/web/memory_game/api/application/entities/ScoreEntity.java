package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table( name = "score",
        uniqueConstraints = { @UniqueConstraint( name = "unique_score_memory_game",
                                                 columnNames = { "score", "memory_game_id" } ) }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( of = "id" )
public class ScoreEntity {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    @Column( nullable = false )
    private Integer score;
    
//    @ManyToOne
//    @JoinColumn( name = "user_id", nullable = false )
//    private UserEntity user;
    
    @ManyToOne
    @JoinColumn( name = "memory_game_id", nullable = false )
    private MemoryGameEntity memoryGame;
}