package com.tcc.jogodememoria.backend.models;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class UserModel {

	/*
	 * public UserModel() {}
	 * 
	 * public UserModel(UserModel user) { this.id = new
	 * UUID(user.getId().getMostSignificantBits(),
	 * user.getId().getLeastSignificantBits()); this.name = new
	 * String(user.getName()); this.username = new String(user.getUsername());
	 * this.email = new String(user.getEmail()); this.password = new
	 * String(user.getPassword()); this.subjects = new
	 * HashSet<SubjectModel>(user.getSubjects()); this.userType =
	 * user.getUserType(); this.memoryGames = }
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_SUBJECT", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private Set<SubjectModel> subjects;

	@ManyToOne
	@JoinColumn(name = "user_type_id", nullable = false)
	private UserTypeModel userType;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Set<MemoryGameModel> memoryGames;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Set<ScoreModel> scores;
}