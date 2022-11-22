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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MEMORY_GAMES", uniqueConstraints = {
                @UniqueConstraint(name = "unique_name_user", columnNames = { "name", "user_id" })
})
public class MemoryGameModel {

	/*
	 * public MemoryGameModel() { }
	 * 
	 * public MemoryGameModel(final MemoryGameModel memoryGame) { id = new
	 * UUID(memoryGame.getId().getMostSignificantBits(),
	 * memoryGame.getId().getLeastSignificantBits()); name = new
	 * String(memoryGame.getName()); user = memoryGame.getUser(); subjects = new
	 * HashSet<SubjectModel>(memoryGame.getSubjects()); cards = new
	 * HashSet<CardModel>(memoryGame.getCards()); scores = new
	 * HashSet<ScoreModel>(memoryGame.getScores()); }
	 */

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @NotBlank
        @Column(nullable = false)
        private String name;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private UserModel user;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "MEMORY_GAME_SUBJECT", joinColumns = @JoinColumn(name = "memory_game_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
        private Set<SubjectModel> subjects;

        @OneToMany(mappedBy = "memoryGame", fetch = FetchType.EAGER)
        private Set<CardModel> cards;

        @OneToMany(mappedBy = "memoryGame", fetch = FetchType.EAGER)
        private Set<ScoreModel> scores;
}