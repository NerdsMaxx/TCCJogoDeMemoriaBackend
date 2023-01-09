package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.insert.CardInsertDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.update.CardUpdateDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CardRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    @Transactional
    public Set<CardEntity> registerNewCardsForMemoryGame( Set<CardInsertDto> cardInsertDtoSet, MemoryGameEntity memoryGame, UserEntity user ) {
        var cardSet = new HashSet<CardEntity>();
        
        for ( var cardInsertDto : cardInsertDtoSet ) {
            var card = cardMapper.convertInsertDtoToEntity( cardInsertDto );
            saveCard( card, memoryGame, user );
            cardSet.add( card );
        }
        
        return cardSet;
    }
    
    @Transactional
    public Set<CardEntity> updateCardsForMemoryGame( Set<CardUpdateDto> cardUpdateDtoSet, MemoryGameEntity memoryGame, UserEntity user ) {
        deleteCardsByMemoryGameAndUser( memoryGame, user );
        
        var cardSet = new HashSet<CardEntity>();
        for ( var cardUpdateDto : cardUpdateDtoSet ) {
            var card = cardMapper.convertUpdateDtoToEntity( cardUpdateDto );
            saveCard( card, memoryGame, user );
            cardSet.add( card );
        }
        
        return cardSet;
    }
    
    private void saveCard(CardEntity card, MemoryGameEntity memoryGame, UserEntity user){
        card.setMemoryGame( memoryGame );
        card.setUser( user );
        cardRepository.save( card );
    }
    
    private void deleteCardsByMemoryGameAndUser(MemoryGameEntity memoryGame, UserEntity user) {
        List<CardEntity> cardList = cardRepository.findAllByMemoryGameAndUser( memoryGame, user );
        cardRepository.deleteAllInBatch(cardList);
    }
}