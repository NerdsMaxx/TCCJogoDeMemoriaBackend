package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table( name = "subject" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode( of = "id" )
public class SubjectEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    @NonNull
    @Column( nullable = false, unique = true )
    private String subject;
    
    @ManyToMany( mappedBy = "subjectSet", fetch = FetchType.EAGER )
    private Set<UserEntity> userSet;
    
    @ManyToMany( mappedBy = "subjectSet", fetch = FetchType.EAGER )
    private Set<MemoryGameEntity> memoryGameSet;
    
}