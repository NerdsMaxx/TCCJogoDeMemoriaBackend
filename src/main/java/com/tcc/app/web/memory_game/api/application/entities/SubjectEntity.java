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
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table( name = "subject" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class SubjectEntity {

		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;
		
		@NonNull
		@Column( nullable = false, unique = true )
		private String subject;

		@ManyToMany(fetch = FetchType.EAGER)
		@JoinTable( name = "user_subject", joinColumns = @JoinColumn( name = "subject_id" ),
		inverseJoinColumns = @JoinColumn( name = "user_id" ) )
		private Set<UserEntity> userSet;

		@ManyToMany( mappedBy = "subjectSet", fetch = FetchType.EAGER)
		private Set<MemoryGameEntity> memoryGameSet;

}