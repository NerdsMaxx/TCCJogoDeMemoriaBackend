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
@Data
@EqualsAndHashCode(of = "id")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_type",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "type_user_id"))
    private Set<UserTypeEntity> userType;
    
    @OneToMany(mappedBy = "creator")
    private Set<MemoryGameEntity> memoryGameCreatorSet = new HashSet<>();
    
//    @ManyToMany(mappedBy = "playerSet")
//    private Set<MemoryGameEntity> memoryGamePlayerSet = new HashSet<>();
    
    @OneToMany(mappedBy = "player")
    private Set<PlayerGameplayEntity> playerGameplaySet = new HashSet<>();
    
    public boolean isCreator() {
        return userType.stream().anyMatch(userType -> UserTypeEnum.CRIADOR.equals(userType.getType()));
    }
    
    public boolean isPlayer() {
        return userType.stream().anyMatch(userType -> UserTypeEnum.JOGADOR.equals(userType.getType()));
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userType;
    }
    
    @Override
    public String getUsername() {
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