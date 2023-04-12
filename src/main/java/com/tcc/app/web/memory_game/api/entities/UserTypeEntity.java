package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "type_user")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")

public class UserTypeEntity implements GrantedAuthority {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private final UserTypeEnum type;
    
    @NonNull
    @ManyToMany(mappedBy = "userType")
    private final Set<UserEntity> user;
    
    @Override
    public String getAuthority() {
        return "ROLE_" + this.type;
    }
}