package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    public List<CardEntity> saveCards(List<CardRequestDto> cardRequestDtoList, MemoryGameEntity memoryGame) {
        List<CardEntity> cardList = new LinkedList<>();
        
        for (CardRequestDto cardRequestDto : cardRequestDtoList) {
            CardEntity card = cardMapper.toCardEntity(cardRequestDto);
            saveCard(card, memoryGame);
            cardList.add(card);
        }
        
        return cardList;
    }
    
    public List<CardEntity> updateCards(List<CardRequestDto> cardRequestDtoList, MemoryGameEntity memoryGame, CreatorEntity creator) {
        deleteCardsByMemoryGameAndUserInBatch(memoryGame, creator);
        return saveCards(cardRequestDtoList, memoryGame);
    }
    
    public void deleteCardsByMemoryGameAndUserInBatch(MemoryGameEntity memoryGame, CreatorEntity creator) {
        List<CardEntity> cardList = cardRepository.findAllByMemoryGameAndCreator(memoryGame, creator);
        cardRepository.deleteAllInBatch(cardList);
    }
    
    public void deleteCardsByMemoryGameAndUser(MemoryGameEntity memoryGame, CreatorEntity creator) {
        List<CardEntity> cardList = cardRepository.findAllByMemoryGameAndCreator(memoryGame, creator);
        cardRepository.deleteAll(cardList);
    }
    
    private void saveCard(CardEntity card, MemoryGameEntity memoryGame) {
        card.setMemoryGame(memoryGame);
        cardRepository.save(card);
    }
}