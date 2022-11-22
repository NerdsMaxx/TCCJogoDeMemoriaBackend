package com.tcc.jogodememoria.backend.interfaces.services;

import java.util.List;
import java.util.Optional;

import com.tcc.jogodememoria.backend.interfaces.IService;
import com.tcc.jogodememoria.backend.models.CardModel;
import com.tcc.jogodememoria.backend.models.MemoryGameModel;

public interface ICardService extends IService<CardModel> {
    boolean existsByQuestionAndAnswerAndMemoryGame (String question, String answer, MemoryGameModel memoryGame);
    
    Optional<CardModel> findByQuestionAndAnswerAndMemoryGame (String question, String answer, MemoryGameModel memoryGame);

    List<CardModel> findByMemoryGame(MemoryGameModel memoryGame);

	void deleteAllInBatch(Iterable<CardModel> cardIterable);
}