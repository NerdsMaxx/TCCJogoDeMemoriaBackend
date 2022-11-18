package com.tcc.jogodememoria.backend.interfaces.services;

import com.tcc.jogodememoria.backend.interfaces.IService;
import com.tcc.jogodememoria.backend.models.CardModel;
import com.tcc.jogodememoria.backend.models.MemoryGameModel;

import java.util.Optional;

public interface ICardService extends IService<CardModel> {
    boolean existsByQuestionAndAnswerAndMemoryGame (String question, String answer, MemoryGameModel memoryGame);
    
    Optional<CardModel> findByQuestionAndAnswerAndMemoryGame (String question, String answer, MemoryGameModel memoryGame);
}