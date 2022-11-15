package com.tcc.jogodememoria.backend.features.user.models;

import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.features.score.models.ScoreModel;
import com.tcc.jogodememoria.backend.features.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.features.userType.models.UserTypeModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_SUBJECT",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<SubjectModel> subjects;
    
    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserTypeModel userType;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<MemoryGameModel> memoryGames;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<ScoreModel> scores;
}
