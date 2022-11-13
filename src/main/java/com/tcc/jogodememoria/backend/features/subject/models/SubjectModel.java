package com.tcc.jogodememoria.backend.features.subject.models;

import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "SUBJECTS")
public class SubjectModel {

    public SubjectModel() {
    }

    public SubjectModel(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    private Set<UserModel> users;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    private Set<MemoryGameModel> memoryGames;
}
