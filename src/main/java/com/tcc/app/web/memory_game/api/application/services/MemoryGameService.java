package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerMemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.*;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CardRepository;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemoryGameService {
    
    
    @Autowired
    private CreatorService creatorService;
    
    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    @Autowired
    private AuthenticatedUserUtil authenticatedUserUtil;
    
    public Set<MemoryGameEntity> findAll() throws Exception {
        
        if (authenticatedUserUtil.isCreator()) {
            CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
            return memoryGameRepository.findAllByCreator(creator);
        }
        
        if (authenticatedUserUtil.isPlayer()) {
            PlayerEntity player = authenticatedUserUtil.getCurrentPlayer();
            return memoryGameRepository.findAllByPlayer( player);
        }
        
        return Collections.emptySet();
    }
    
    public Set<MemoryGameEntity> findByMemoryGameNameAndSubject(String search) throws Exception {
        if(authenticatedUserUtil.isCreator()) {
            CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
            return memoryGameRepository.findAllBySubjectOrMemoryGameName( creator, search, search);
        }
        
        if(authenticatedUserUtil.isPlayer()) {
            PlayerEntity player = authenticatedUserUtil.getCurrentPlayer();
            return memoryGameRepository.findAllBySubjectOrMemoryGameName(player, search, search);
        }
        
        return Collections.emptySet();
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(CreatorEntity creator, String memoryGameName) throws Exception {
        return memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException("Não tem este jogo de memória!"));
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(String memoryGameName) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        return findByCreatorAndMemoryGame(creator, memoryGameName);
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(String memoryGameName, String creatorUsername) throws Exception {
        return memoryGameRepository.findByCreatorUsernameAndMemoryGame(creatorUsername, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException("Jogo de memória não encontrada!"));
    }
    
    public CardEntity findCardById(Long id) throws Exception {
        return cardRepository.findById(id)
                             .orElseThrow(() -> new EntityNotFoundException("Não tem esta carta!"));
    }
    
    public MemoryGameEntity save(MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        if (memoryGameRepository.existsByCreatorAndMemoryGame(creator, memoryGameRequestDto.name())) {
            throw new EntityExistsException("Já existe o jogo de memória com este nome.");
        }
        
        Set<SubjectEntity> subjectFoundSet = subjectRepository.findBySubjectSet(memoryGameRequestDto.subjectSet());
        
        MemoryGameEntity memoryGame = new MemoryGameEntity();
        memoryGame.setMemoryGame(memoryGameRequestDto.name())
                  .setCreator(creator)
                  .addCardListFromResquestDto(memoryGameRequestDto.cardSet(), cardMapper)
                  .addSubjectListFromRequestDto(memoryGameRequestDto.subjectSet(), subjectFoundSet);
        
        return memoryGameRepository.save(memoryGame);
    }
    
    public MemoryGameEntity update(String memoryGameName, MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "Não foi encontrado jogo de memória."));
        
        String name = memoryGameRequestDto.name();
        if (name != null) {
            memoryGame.setMemoryGame(name);
            memoryGameRepository.save(memoryGame);
        }
        
        Set<CardRequestDto> cardSet = memoryGameRequestDto.cardSet();
        if (cardSet != null) {
            memoryGame.clearCardSet(cardSet, cardMapper)
                      .addCardListFromResquestDto(cardSet, cardMapper);
        }
        
        Set<String> subjectSet = memoryGameRequestDto.subjectSet();
        if (subjectSet != null) {
            _deleteAllSubjectNotUsed(memoryGame, subjectSet);
            memoryGame.clearSubjectSet(subjectSet);
            
            Set<SubjectEntity> subjectFoundSet = subjectRepository.findBySubjectSet(subjectSet);
            memoryGame.addSubjectListFromRequestDto(subjectSet, subjectFoundSet);
        }
        
        return memoryGameRepository.save(memoryGame);
    }
    
    public void delete(String memoryGameName) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "Não foi encontrado jogo de memória."));
        
        _deleteAllSubjectNotUsed(memoryGame);
        
        memoryGame.clearCardSet();
        memoryGame.clearSubjectSet();
        memoryGameRepository.delete(memoryGame);
    }
    
    public String addPlayer(PlayerMemoryGameRequestDto playerMemoryGameRequestDto) throws Exception {
        CreatorEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        String memoryGameName = playerMemoryGameRequestDto.memoryGameName();
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "Não foi encontrado jogo de memória."));
        
        PlayerEntity player = playerService.findByUsername(playerMemoryGameRequestDto.username());
        memoryGame.addPlayer(player);
        memoryGameRepository.save(memoryGame);
        
        return "Jogador adicionado com sucesso!";
    }
    
    public void addPlayer(MemoryGameEntity memoryGame, PlayerEntity player) {
        memoryGame.addPlayer(player);
        memoryGameRepository.save(memoryGame);
    }
    
    public void _deleteAllSubjectNotUsed(MemoryGameEntity memoryGame, Set<String> subjectNameSet) {
        Set<SubjectEntity> subjectSet;
        
        if (subjectNameSet != null && ! subjectNameSet.isEmpty()) {
            subjectSet = memoryGame.getSubjectSet().stream()
                                   .filter(subject -> ! subjectNameSet.contains(subject.getSubject()))
                                   .collect(Collectors.toSet());
        } else {
            subjectSet = new HashSet<>(memoryGame.getSubjectSet());
        }
        
        subjectSet.forEach(subject -> subject.removeMemoryGame(memoryGame));
        subjectSet = subjectSet.stream()
                               .filter(subject -> subject.getMemoryGameSet().isEmpty())
                               .collect(Collectors.toSet());
        subjectRepository.deleteAll(subjectSet);
    }
    
    public void _deleteAllSubjectNotUsed(MemoryGameEntity memoryGame) {
        _deleteAllSubjectNotUsed(memoryGame, null);
    }
}