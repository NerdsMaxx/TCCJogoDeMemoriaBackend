package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CardRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    @Transactional
    public List<CardEntity> saveCards(List<CardRequestDto> cardRequestDtoList, MemoryGameEntity memoryGame) {
        var cardList = new LinkedList<CardEntity>();
        
        for (var cardRequestDto : cardRequestDtoList) {
            var card = cardMapper.toCardEntity(cardRequestDto);
            saveCard(card, memoryGame);
            cardList.add(card);
        }
        
        return cardList;
    }
    
    @Transactional
    public List<CardEntity> updateCards(List<CardRequestDto> cardRequestDtoList, MemoryGameEntity memoryGame, UserEntity user) {
        deleteCardsByMemoryGameAndUserInBatch(memoryGame, user);
        return saveCards(cardRequestDtoList, memoryGame);
    }
    
    @Transactional
    public void deleteCardsByMemoryGameAndUserInBatch(MemoryGameEntity memoryGame, UserEntity user) {
        List<CardEntity> cardList = cardRepository.findAllByMemoryGameAndUser(memoryGame, user);
        cardRepository.deleteAllInBatch(cardList);
    }
    
    @Transactional
    public void deleteCardsByMemoryGameAndUser(MemoryGameEntity memoryGame, UserEntity user) {
        List<CardEntity> cardList = cardRepository.findAllByMemoryGameAndUser(memoryGame, user);
        cardRepository.deleteAll(cardList);
    }
    
    @Transactional
    private void saveCard(CardEntity card, MemoryGameEntity memoryGame) {
        card.setMemoryGame(memoryGame);
        cardRepository.save(card);
    }
}