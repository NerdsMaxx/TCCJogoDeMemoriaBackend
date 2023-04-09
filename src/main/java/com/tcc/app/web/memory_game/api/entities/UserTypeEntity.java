package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "type_user")
@Data
@EqualsAndHashCode(of = "id")
public class UserTypeEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserTypeEnum type;
    
    @ManyToMany(mappedBy = "userType")
    private Set<UserEntity> user;
    
    @Override
    public String getAuthority() {
        return "ROLE_" + this.type;
    }
}