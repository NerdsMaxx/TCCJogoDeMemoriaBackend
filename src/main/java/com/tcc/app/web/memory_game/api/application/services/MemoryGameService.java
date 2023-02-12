package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerMemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemoryGameService {
    
    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    @Autowired
    private AuthenticatedUserUtil authenticatedUserUtil;
    
    public Page<MemoryGameEntity> findAllByCreator(Pageable pageable) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        return memoryGameRepository.findAllByCreator(pageable, creator);
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(CreatorEntity creator, String memoryGameName) throws Exception {
        return memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException("Não tem este jogo de memória!"));
    }
    
    public MemoryGameEntity save(MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        //verify if memory game exists
        if (memoryGameRepository.existsByCreatorAndMemoryGame(creator, memoryGameRequestDto.name())) {
            throw new EntityExistsException("Já existe o jogo de memória com este nome.");
        }
        
        //memory game and user
        MemoryGameEntity memoryGame = new MemoryGameEntity();
        memoryGame.setMemoryGame(memoryGameRequestDto.name());
        memoryGame.setCreator(creator);
        
        memoryGameRepository.save(memoryGame);
        
        List<CardEntity> cardList = cardService.saveCards(memoryGameRequestDto.cardList(), memoryGame);
        memoryGame.setCardList(cardList);
        
        List<SubjectEntity> subjectList = subjectService.save(memoryGameRequestDto.subjectList(), memoryGame);
        memoryGame.setSubjectList(subjectList);
        
        return memoryGame;
    }
    
    public MemoryGameEntity update(String memoryGameName, MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "Não foi encontrado jogo de memória."));
        
        if (memoryGameRequestDto.name() != null) {
            memoryGame.setMemoryGame(memoryGameRequestDto.name());
            memoryGameRepository.save(memoryGame);
        }
        
        if (memoryGameRequestDto.cardList() != null) {
            List<CardEntity> cardList = cardService.updateCards(memoryGameRequestDto.cardList(), memoryGame, creator);
            memoryGame.setCardList(cardList);
        }
        
        if (memoryGameRequestDto.subjectList() != null) {
            List<SubjectEntity> subjectList = subjectService.update(memoryGameRequestDto.subjectList(),
                                                                    memoryGame);
            memoryGame.setSubjectList(subjectList);
        }
        
        return memoryGame;
    }
    
    public String addPlayer(PlayerMemoryGameRequestDto playerMemoryGameRequestDto) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        String memoryGameName = playerMemoryGameRequestDto.memoryGameName();
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "Não foi encontrado jogo de memória."));
        
        String usernamePlayer = playerMemoryGameRequestDto.username();
        
        playerService.addMemoryGame(usernamePlayer, memoryGame);
        
        return "Jogador adicionado com sucesso!";
    }
    
    public void delete(String memoryGameName) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "Não foi encontrado jogo de memória."));
        
        subjectService.deleteByMemoryGameAndUser(memoryGame);
        cardService.deleteCardsByMemoryGameAndUser(memoryGame, creator);
        
        memoryGameRepository.delete(memoryGame);
    }
}