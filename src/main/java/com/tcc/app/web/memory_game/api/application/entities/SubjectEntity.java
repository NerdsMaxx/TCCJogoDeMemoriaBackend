package com.tcc.app.web.memory_game.api.application.entities;

import java.util.Set;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "subject" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode( of = "id" )
public class SubjectEntity {

		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@NonNull
		@Column( nullable = false, unique = true )
		private String name;

//		@ManyToMany(fetch = FetchType.EAGER)
//		@JoinTable( name = "user_subject", joinColumns = @JoinColumn( name = "subject_id" ),
//		inverseJoinColumns = @JoinColumn( name = "user_id" ) )
		@ManyToMany( mappedBy = "subjectSet", fetch = FetchType.EAGER )
		private Set<UserEntity> userSet;

		@ManyToMany( mappedBy = "subjectSet", fetch = FetchType.EAGER )
		private Set<MemoryGameEntity> memoryGameSet;

}
