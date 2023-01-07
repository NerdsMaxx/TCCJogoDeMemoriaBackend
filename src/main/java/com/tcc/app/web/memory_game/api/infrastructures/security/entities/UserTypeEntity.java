package com.tcc.app.web.memory_game.api.infrastructures.security.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table( name = "user_type" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserTypeEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String type;
    
    @OneToMany(mappedBy = "userType")
    private Set<UserEntity> user;
}