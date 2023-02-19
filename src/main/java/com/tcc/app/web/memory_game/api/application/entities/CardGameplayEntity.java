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
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class CardGameplayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Boolean winner;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_gameplay_id", nullable = false)
    private PlayerGameplayEntity playerGameplay;
    
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;
    
    public boolean equalsCardId(Long id) {
        return this.card.getId().equals(id);
    }
}