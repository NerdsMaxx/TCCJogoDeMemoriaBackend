package com.tcc.jogodememoria.backend.features.card.interfaces;

import com.tcc.jogodememoria.backend.features.card.models.CardModel;
import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICardRepository extends JpaRepository<CardModel, UUID> {
    boolean existsByQuestionAndAnswerAndMemoryGame(String question, String answer, MemoryGameModel memoryGame);

    Optional<CardModel> findByQuestionAndAnswerAndMemoryGame(String question, String answer, MemoryGameModel memoryGame);
}
