package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemoryGameService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    public Page<MemoryGameEntity> findAllByUser(Pageable pageable) throws Exception {
        var user = userService.getCurrentUser();
        return memoryGameRepository.findAllByUser(pageable, user);
    }
    
    @Transactional
    public MemoryGameEntity saveMemoryGame(MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        var user = userService.getCurrentUser();
        
        //verify if memory game exists
        if (memoryGameRepository.existsByUserAndMemoryGame(user, memoryGameRequestDto.name())) {
            throw new EntityExistsException("Já existe o jogo de memória com este nome.");
        }
        
        //memory game and user
        var memoryGame = new MemoryGameEntity();
        memoryGame.setMemoryGame(memoryGameRequestDto.name());
        memoryGame.setUser(user);
        
        memoryGameRepository.save(memoryGame);
        
        var cardList = cardService.saveCards(memoryGameRequestDto.cardList(), memoryGame);
        memoryGame.setCardList(cardList);
        
        var subjectList = subjectService.saveSubjects(memoryGameRequestDto.subjectList(), memoryGame, user);
        memoryGame.setSubjectList(subjectList);
        
        return memoryGame;
    }
    
    @Transactional
    public MemoryGameEntity updateMemoryGame(String memoryGameName, MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        var user = userService.getCurrentUser();
        
        var memoryGame = memoryGameRepository.findByUserAndMemoryGame(user, memoryGameName)
                                             .orElseThrow(() -> new EntityNotFoundException(
                                                     "Não foi encontrado jogo de memória."));
        
        if (memoryGameRequestDto.name() != null) {
            memoryGame.setMemoryGame(memoryGameRequestDto.name());
            memoryGameRepository.save(memoryGame);
        }
        
        if (memoryGameRequestDto.cardList() != null) {
            var cardList = cardService.updateCards(memoryGameRequestDto.cardList(), memoryGame, user);
            memoryGame.setCardList(cardList);
        }
        
        if (memoryGameRequestDto.subjectList() != null) {
            var subjectList = subjectService.updateSubjects(memoryGameRequestDto.subjectList(), memoryGame);
            memoryGame.setSubjectList(subjectList);
        }
        
        return memoryGame;
    }
    
    @Transactional
    public void deleteMemoryGame(String memoryGameName) throws Exception {
        var user = userService.getCurrentUser();
        
        var memoryGame = memoryGameRepository.findByUserAndMemoryGame(user, memoryGameName)
                                             .orElseThrow(() -> new EntityNotFoundException(
                                                     "Não foi encontrado jogo de memória."));
        
        subjectService.deleteSubjectsByMemoryGameAndUser(memoryGame);
        cardService.deleteCardsByMemoryGameAndUser(memoryGame, user);
        
        memoryGameRepository.delete(memoryGame);
    }
}