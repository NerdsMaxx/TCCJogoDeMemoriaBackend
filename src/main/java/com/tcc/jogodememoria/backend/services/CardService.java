package com.tcc.jogodememoria.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tcc.jogodememoria.backend.interfaces.repositories.ICardRepository;
import com.tcc.jogodememoria.backend.interfaces.services.ICardService;
import com.tcc.jogodememoria.backend.models.CardModel;
import com.tcc.jogodememoria.backend.models.MemoryGameModel;

@Service
public class CardService implements ICardService {
    
    final ICardRepository cardRepo;
    
    public CardService (final ICardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }
    
    @Override
    public boolean existsByQuestionAndAnswerAndMemoryGame (final String question, final String answer, final MemoryGameModel memoryGame) {
        return cardRepo.existsByQuestionAndAnswerAndMemoryGame(question, answer, memoryGame);
    }
    
    @Override
    public Optional<CardModel> findByQuestionAndAnswerAndMemoryGame (final String question, final String answer, final MemoryGameModel memoryGame) {
        return cardRepo.findByQuestionAndAnswerAndMemoryGame(question, answer, memoryGame);
    }
    
    @Override
	public List<CardModel> findByMemoryGame(final MemoryGameModel memoryGame) {
		return cardRepo.findByMemoryGame(memoryGame);
	}
    
    @Override
    public CardModel save (final CardModel card) {
        return cardRepo.save(card);
    }
    
    @Override
    public void delete (final CardModel card) {
        cardRepo.delete(card);
    }
    
    @Override
    public void deleteAllInBatch(final Iterable<CardModel> cardIterable) {
    	cardRepo.deleteAllInBatch(cardIterable);
    }
}