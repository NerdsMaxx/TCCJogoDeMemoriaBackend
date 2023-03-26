package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerMemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CardRepository;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.custom.CustomException;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.UserTypeUtilStatic;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemoryGameService {
    
    private static final String MEMORY_GAME_NOT_FOUND = "Não foi encontrado o jogo de memória.";
    
    @Autowired
    private UserService userService;
    
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
    
    public Set<MemoryGameEntity> findAll(Set<UserTypeEnum> userTypeEnumSet) throws Exception {
        UserEntity user = authenticatedUserUtil.getCurrentUser();
        Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
        
        if (userTypeEnumSet.contains(UserTypeEnum.CRIADOR) && user.isCreator()) {
            memoryGameSet.addAll(memoryGameRepository.findAllByCreator(user));
        }
        
        if (userTypeEnumSet.contains(UserTypeEnum.JOGADOR) && user.isPlayer()) {
            memoryGameSet.addAll(memoryGameRepository.findAllByPlayer(user));
        }
        
        return memoryGameSet;
    }
    
    public Set<MemoryGameEntity> findByMemoryGameNameAndSubject(String search, Set<UserTypeEnum> userTypeEnumSet) throws Exception {
        UserEntity user = authenticatedUserUtil.getCurrentUser();
        Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
        
        if (userTypeEnumSet.contains(UserTypeEnum.CRIADOR) && user.isCreator()) {
            memoryGameSet.addAll(memoryGameRepository.findAllBySubjectOrMemoryGameNameForCreator(user, search, search));
        }
        
        if (userTypeEnumSet.contains(UserTypeEnum.JOGADOR) && user.isPlayer()) {
            memoryGameSet.addAll(memoryGameRepository.findAllBySubjectOrMemoryGameNameForPlayer(user, search, search));
        }
        
        return memoryGameSet;
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(UserEntity creator, String memoryGameName) throws Exception {
        UserTypeUtilStatic.throwIfUserIsNotCreator(creator);
        return memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException(MEMORY_GAME_NOT_FOUND));
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(String memoryGameName) throws Exception {
        UserEntity creator = authenticatedUserUtil.getCurrentCreator();
        return findByCreatorAndMemoryGame(creator, memoryGameName);
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(String memoryGameName, String creatorUsername) throws Exception {
        return memoryGameRepository.findByCreatorUsernameAndMemoryGame(creatorUsername, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException(MEMORY_GAME_NOT_FOUND));
    }
    
    public CardEntity findCardById(Long id) throws Exception {
        return cardRepository.findById(id)
                             .orElseThrow(() -> new EntityNotFoundException("Não tem esta carta!"));
    }
    
    public MemoryGameEntity save(MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        UserEntity creator = authenticatedUserUtil.getCurrentCreator();
        
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
        UserEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  MEMORY_GAME_NOT_FOUND));
        
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
        UserEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  MEMORY_GAME_NOT_FOUND));
        
        _deleteAllSubjectNotUsed(memoryGame);
        
        memoryGame.clearCardSet();
        memoryGame.clearSubjectSet();
        memoryGameRepository.delete(memoryGame);
    }
    
    public String addPlayer(PlayerMemoryGameRequestDto playerMemoryGameRequestDto) throws Exception {
        UserEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        String memoryGameName = playerMemoryGameRequestDto.memoryGameName();
        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  MEMORY_GAME_NOT_FOUND));
        
        UserEntity player = userService.findPlayerByUsernameOrEmail(playerMemoryGameRequestDto.username())
                                       .orElseThrow(() -> new CustomException("Não foi encontrado o jogador."));
        
        memoryGame.addPlayer(player);
        memoryGameRepository.save(memoryGame);
        
        return "Jogador adicionado com sucesso!";
    }
    
    public void addPlayer(MemoryGameEntity memoryGame, UserEntity player) throws CustomException {
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