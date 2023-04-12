package com.tcc.app.web.memory_game.api.entities;

import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_mg")

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")

public class UserEntity implements UserDetails {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    @Column(nullable = false)
    private final String name;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private final String email;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private final String username;
    
    @Setter
    @Column(nullable = false)
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_type",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "type_user_id"))
    private final Set<UserTypeEntity> userType = new HashSet<>();
    
    @OneToMany(mappedBy = "creator")
    private final Set<MemoryGameEntity> memoryGameCreatorSet = new HashSet<>();
    
    
    @OneToMany(mappedBy = "player")
    private final Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    public boolean isCreator() {
        return userType.stream().anyMatch(userType1 -> UserTypeEnum.CRIADOR.equals(userType1.getType()));
    }
    
    public boolean isPlayer() {
        return userType.stream().anyMatch(userType1 -> UserTypeEnum.JOGADOR.equals(userType1.getType()));
    }
    
    public UserEntity addTypes(Set<UserTypeEntity> types) {
        userType.addAll(types);
        return this;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userType;
    }
    
    @Override
    public @NonNull String getUsername() {
        return username;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}