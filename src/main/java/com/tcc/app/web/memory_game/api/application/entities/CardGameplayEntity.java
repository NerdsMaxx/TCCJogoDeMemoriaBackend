package com.tcc.app.web.memory_game.api.application.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_gameplay",
       uniqueConstraints = {@UniqueConstraint(name = "unique_player_gameplay_card",
                                              columnNames = {"player_gameplay_id", "card_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class CardGameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Boolean winner;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_gameplay_id", nullable = false)
    private PlayerGameplayEntity playerGameplay;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity cardEntity;
}