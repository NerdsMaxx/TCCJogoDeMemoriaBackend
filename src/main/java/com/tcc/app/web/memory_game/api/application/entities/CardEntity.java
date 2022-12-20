package com.tcc.app.web.memory_game.api.application.entities;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "card", uniqueConstraints = {@UniqueConstraint(
                name = "unique_first_content_second_content_memory_game",
                columnNames = {"first_content", "second_content", "memory_game_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CardEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, name = "first_content")
        private String firstContent;

        @Column(nullable = false, name = "second_content")
        private String secondContent;

        @ManyToOne
        @JoinColumn(name = "memory_game_id", nullable = false)
        private MemoryGameEntity memoryGame;

}
