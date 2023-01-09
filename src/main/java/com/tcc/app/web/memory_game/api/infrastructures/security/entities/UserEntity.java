package com.tcc.app.web.memory_game.api.infrastructures.security.entities;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.ScoreEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "user_mg" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserEntity implements UserDetails {
		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@Column( nullable = false )
		private String name;

		@Column( nullable = false )
		private String email;

		private String username;
		private String password;
		
		@ManyToOne
		@JoinColumn(name = "user_type_id")
		private UserTypeEntity userType;

		@ManyToMany( fetch = FetchType.EAGER )
		@JoinTable( name = "user_subject", joinColumns = @JoinColumn( name = "user_id" ),
					inverseJoinColumns = @JoinColumn( name = "subject_id" ) )
		private Set<SubjectEntity> subjectSet;

		@OneToMany( mappedBy = "user" )
		private Set<MemoryGameEntity> memoryGameSet;

		@OneToMany( mappedBy = "user" )
		private Set<ScoreEntity> scoreSet;

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
				return List.of( new SimpleGrantedAuthority( "ROLE_USER" ) );
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