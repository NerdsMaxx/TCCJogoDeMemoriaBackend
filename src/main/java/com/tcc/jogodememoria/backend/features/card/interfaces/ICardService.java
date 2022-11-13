package com.tcc.jogodememoria.backend.features.card.interfaces;

import com.tcc.jogodememoria.backend.features.card.models.CardModel;
import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.interfaces.IService;

import java.util.Optional;

public interface ICardService extends IService<CardModel> {
    boolean existsByQuestionAndAnswerAndMemoryGame(String question, String answer, MemoryGameModel memoryGame);

    Optional<CardModel> findByQuestionAndAnswerAndMemoryGame(String question, String answer, MemoryGameModel memoryGame);
}
