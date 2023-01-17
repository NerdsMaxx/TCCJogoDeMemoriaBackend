package com.tcc.app.web.memory_game.api.infrastructures.security.entities;

import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.ScoreEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "user_mg")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    private String username;
    private String password;
    
    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserTypeEntity userType;
    
    @ManyToMany(mappedBy = "userList")
    private List<SubjectEntity> subjectList = new LinkedList<>();
    
    @OneToMany(mappedBy = "user")
    private List<MemoryGameEntity> memoryGameList = new LinkedList<>();
    
    @OneToMany(mappedBy = "user")
    private List<ScoreEntity> scoreList = new LinkedList<>();
    
    @OneToMany(mappedBy = "user")
    private List<CardEntity> cardList = new LinkedList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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