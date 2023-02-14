package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerMemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    private PlayerService playerService;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
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
        
        if (memoryGameRepository.existsByCreatorAndMemoryGame(creator, memoryGameRequestDto.name())) {
            throw new EntityExistsException("Já existe o jogo de memória com este nome.");
        }
        
        MemoryGameEntity memoryGame = new MemoryGameEntity(memoryGameRequestDto.name(), creator);
        memoryGame.addCardListFromResquestDto(memoryGameRequestDto.cardSet(), cardMapper);
        
        Set<SubjectEntity> subjectFoundSet = subjectRepository.findBySubjectSet(memoryGameRequestDto.subjectSet());
        memoryGame.addSubjectListFromRequestDto(memoryGameRequestDto.subjectSet(), subjectFoundSet);
        
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
            memoryGame.clearCardSet();
            memoryGame.addCardListFromResquestDto(cardSet, cardMapper);
        }
        
        Set<String> subjectSet = memoryGameRequestDto.subjectSet();
        if (subjectSet != null) {
            _deleteAllSubjectNotUsed(memoryGame, subjectSet);
            memoryGame.clearSubjectSet();
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
        
        if(subjectNameSet != null && !subjectNameSet.isEmpty()) {
            subjectSet = memoryGame.getSubjectSet().stream()
                                   .filter(subject -> !subjectNameSet.contains(subject.getSubject()))
                                   .collect(Collectors.toSet());
        }
        else {
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