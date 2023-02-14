package com.tcc.app.web.memory_game.api.infrastructures.security.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Entity
@Table( name = "user_type" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserTypeEntity implements GrantedAuthority {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserTypeEnum type;
    
    @OneToMany(mappedBy = "userType")
    private Set<UserEntity> user;
    
    @Override
    public String getAuthority() {
        return "ROLE_" + this.type;
    }
    
    public boolean isCreator() {
        return  UserTypeEnum.CRIADOR.equals(type);
    }
    
    public boolean isPlayer() {
        return UserTypeEnum.JOGADOR.equals(type);
    }
}