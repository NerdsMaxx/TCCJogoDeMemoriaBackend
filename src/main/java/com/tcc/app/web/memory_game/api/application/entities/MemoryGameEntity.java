package com.tcc.app.web.memory_game.api.application.entities;

import java.util.Set;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "memory_game" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( of = "id" )
public class MemoryGameEntity {
		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@NotBlank
		@Column( nullable = false )
		private String name;

		@ManyToOne
		@JoinColumn( name = "user_id", nullable = false )
		private UserEntity user;

		@ManyToMany( fetch = FetchType.EAGER )
		@JoinTable( name = "MEMORY_GAME_SUBJECT", joinColumns = @JoinColumn( name = "memory_game_id" ),
						inverseJoinColumns = @JoinColumn( name = "subject_id" ) )
		private Set<SubjectEntity> subjectSet;

		@OneToMany( fetch = FetchType.EAGER )
		@JoinColumn(name = "memory_game_id")
		private Set<CardEntity> cardSet;

		@OneToMany( mappedBy = "memoryGame", fetch = FetchType.EAGER )
		private Set<ScoreEntity> scoreSet;
}
