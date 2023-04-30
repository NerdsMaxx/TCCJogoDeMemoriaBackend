package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.custom.Default;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "type_user")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(onConstructor = @__(@Default))
@EqualsAndHashCode(of = "id")

public class UserTypeEntity implements GrantedAuthority {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final @NonNull Long id;
    
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private final @NonNull UserTypeEnum type;
    
    
    @ManyToMany(mappedBy = "userType")
    private final @NonNull Set<UserEntity> user;
    
    @Override
    public String getAuthority() {
        return "ROLE_" + this.type;
    }
}