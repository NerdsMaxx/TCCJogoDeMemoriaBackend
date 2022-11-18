package com.tcc.jogodememoria.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "MEMORY_GAMES",
       uniqueConstraints = {
               @UniqueConstraint(
                       name = "unique_name_user",
                       columnNames = {"name", "user_id"}
               )
       }
)
public class MemoryGameModel {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "MEMORY_GAME_SUBJECT",
               joinColumns = @JoinColumn(name = "memory_game_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<SubjectModel> subjects;
    
    @OneToMany(mappedBy = "memoryGame", fetch = FetchType.EAGER)
    private Set<CardModel> cards;
    
    @OneToMany(mappedBy = "memoryGame", fetch = FetchType.EAGER)
    private Set<ScoreModel> scores;
}