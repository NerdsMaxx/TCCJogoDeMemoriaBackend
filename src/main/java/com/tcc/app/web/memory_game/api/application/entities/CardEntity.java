package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table( name = "card",
        uniqueConstraints = { @UniqueConstraint( name = "unique_first_content_second_content_memory_game_user",
                                                 columnNames = { "first_content", "second_content", "memory_game_id", "user_id" } ) } )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( of = "id" )
public class CardEntity {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    @Column( nullable = false, name = "first_content" )
    private String firstContent;
    
    @Column( nullable = false, name = "second_content" )
    private String secondContent;
    
    @ManyToOne
    @JoinColumn( name = "user_id", nullable = false )
    private UserEntity user;
    
    @ManyToOne
    @JoinColumn( name = "memory_game_id", nullable = false )
    private MemoryGameEntity memoryGame;
    
}