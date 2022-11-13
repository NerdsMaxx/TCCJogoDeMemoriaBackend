package com.tcc.jogodememoria.backend.features.card.services;

import com.tcc.jogodememoria.backend.features.card.interfaces.ICardRepository;
import com.tcc.jogodememoria.backend.features.card.interfaces.ICardService;
import com.tcc.jogodememoria.backend.features.card.models.CardModel;
import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService implements ICardService {

    final ICardRepository cardRepo;

    public CardService(ICardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public boolean existsByQuestionAndAnswerAndMemoryGame(String question, String answer, MemoryGameModel memoryGame) {
        return cardRepo.existsByQuestionAndAnswerAndMemoryGame(question, answer, memoryGame);
    }

    @Override
    public Optional<CardModel> findByQuestionAndAnswerAndMemoryGame(String question, String answer, MemoryGameModel memoryGame) {
        return cardRepo.findByQuestionAndAnswerAndMemoryGame(question, answer, memoryGame);
    }

    @Override
    public CardModel save(CardModel card) {
        return cardRepo.save(card);
    }

    @Override
    public void delete(CardModel card) {
        cardRepo.delete(card);
    }
}
