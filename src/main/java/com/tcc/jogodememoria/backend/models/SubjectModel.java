package com.tcc.jogodememoria.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "SUBJECTS")
public class SubjectModel {
    
    public SubjectModel () {
    }
    
    public SubjectModel (String name) {
        this.name = name;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    private Set<UserModel> users;
    
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    private Set<MemoryGameModel> memoryGames;
}