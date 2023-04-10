package com.tcc.app.web.memory_game.api.services;

import com.tcc.app.web.memory_game.api.dtos.requests.CardRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.entities.CardEntity;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.mappers.CardMapper;
import com.tcc.app.web.memory_game.api.repositories.CardRepository;
import com.tcc.app.web.memory_game.api.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.repositories.SubjectRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class MemoryGameService {
    
    private static final String MEMORY_GAME_NOT_FOUND = "Não foi encontrado o jogo de memória.";
    
    private final UserService userService;
    private final SubjectRepository subjectRepository;
    private final CardRepository cardRepository;
    private final MemoryGameRepository memoryGameRepository;
    private final CardMapper cardMapper;
    
    public Set<MemoryGameEntity> findAll(@NonNull Set<UserTypeEnum> userTypeEnumSet) {
        final UserEntity user = userService.getCurrentUser();
        final Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
        
        if (userTypeEnumSet.contains(UserTypeEnum.CRIADOR) && user.isCreator()) {
            memoryGameSet.addAll(memoryGameRepository.findAllByCreator(user));
        }
        
        if (userTypeEnumSet.contains(UserTypeEnum.JOGADOR) && user.isPlayer()) {
            memoryGameSet.addAll(memoryGameRepository.findAllByPlayer(user));
        }
        
        return memoryGameSet;
    }
    
    public Set<MemoryGameEntity> findByMemoryGameNameAndSubject(@NonNull String search,
                                                                @NonNull Set<UserTypeEnum> userTypeEnumSet) {
        final UserEntity user = userService.getCurrentUser();
        final Set<MemoryGameEntity> memoryGameSet = new HashSet<>();
        
        if (userTypeEnumSet.contains(UserTypeEnum.CRIADOR) && user.isCreator()) {
            memoryGameSet.addAll(memoryGameRepository.findAllBySubjectOrMemoryGameNameForCreator(user, search, search));
        }
        
        if (userTypeEnumSet.contains(UserTypeEnum.JOGADOR) && user.isPlayer()) {
            memoryGameSet.addAll(memoryGameRepository.findAllBySubjectOrMemoryGameNameForPlayer(user, search, search));
        }
        
        return memoryGameSet;
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(@NonNull UserEntity creator,
                                                       @NonNull String memoryGameName) {
        return memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException(MEMORY_GAME_NOT_FOUND));
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(@NonNull String memoryGameName) throws NoPermissionException {
        final UserEntity creator = userService.getCurrentCreator();
        return findByCreatorAndMemoryGame(creator, memoryGameName);
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(@NonNull String memoryGameName,
                                                       @NonNull String creatorUsername) {
        return memoryGameRepository.findByCreatorUsernameAndMemoryGame(creatorUsername, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException(MEMORY_GAME_NOT_FOUND));
    }
    
    public CardEntity findCardById(@NonNull Long id) {
        return cardRepository.findById(id)
                             .orElseThrow(() -> new EntityNotFoundException("Não tem esta carta!"));
    }
    
    public MemoryGameEntity save(@NonNull MemoryGameRequestDto memoryGameRequestDto) throws NoPermissionException {
        final UserEntity creator = userService.getCurrentCreator();
        
        if (memoryGameRepository.existsByCreatorAndMemoryGame(creator, memoryGameRequestDto.name())) {
            throw new EntityExistsException("Já existe o jogo de memória com este nome.");
        }
        
        final MemoryGameEntity memoryGame = new MemoryGameEntity(memoryGameRequestDto.name(), creator);
        memoryGameRepository.save(memoryGame);
        
        final Set<CardEntity> newCardSet;
        newCardSet = memoryGameRequestDto.cardSet().stream()
                                         .map(cardRequestDto -> cardMapper.toCardEntity(cardRequestDto, memoryGame))
                                         .collect(Collectors.toSet());
        
        final Set<SubjectEntity> subjectFoundSet = subjectRepository.findBySubjectSet(memoryGameRequestDto.subjectSet());
        final Set<SubjectEntity> subjectSet = memoryGameRequestDto.subjectSet().stream()
                                                                  .map(SubjectEntity::new)
                                                                  .collect(Collectors.toSet());
        
        memoryGame.addCardSet(newCardSet)
                  .addSubjectList(subjectSet, subjectFoundSet);
        
        return memoryGameRepository.save(memoryGame);
    }
    
    public MemoryGameEntity update(@NonNull String memoryGameName, @NonNull MemoryGameRequestDto memoryGameRequestDto) throws NoPermissionException {
        final UserEntity creator = userService.getCurrentCreator();
        
        final MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                        MEMORY_GAME_NOT_FOUND));
        
        final String name = memoryGameRequestDto.name();
        if (name != null) {
            memoryGame.setMemoryGame(name);
            memoryGameRepository.save(memoryGame);
        }
        
        final Set<CardRequestDto> cardRequestDtoSet = memoryGameRequestDto.cardSet();
        if (cardRequestDtoSet != null) {
            final Set<CardEntity> cardSet;
            cardSet = cardRequestDtoSet.stream()
                                       .map(cardRequestDto -> cardMapper.toCardEntity(cardRequestDto, memoryGame))
                                       .collect(Collectors.toSet());
            
            memoryGame.clearCardSet(cardSet)
                      .addCardSet(cardSet);
        }
        
        final Set<String> subjectNameSet = memoryGameRequestDto.subjectSet();
        if (subjectNameSet != null) {
            _deleteAllSubjectNotUsed(memoryGame, subjectNameSet);
            memoryGame.clearSubjectSet(subjectNameSet);
            
            final Set<SubjectEntity> subjectSet = subjectNameSet.stream()
                                                                .map(SubjectEntity::new)
                                                                .collect(Collectors.toSet());
            
            final Set<SubjectEntity> subjectFoundSet = subjectRepository.findBySubjectSet(subjectNameSet);
            
            memoryGame.addSubjectList(subjectSet, subjectFoundSet);
        }
        
        return memoryGameRepository.save(memoryGame);
    }
    
    public void delete(@NonNull String memoryGameName) throws NoPermissionException {
        final UserEntity creator = userService.getCurrentCreator();
        
        final MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                        MEMORY_GAME_NOT_FOUND));
        
        _deleteAllSubjectNotUsed(memoryGame);
        
        memoryGame.clearCardSet();
        memoryGame.clearSubjectSet();
        memoryGameRepository.delete(memoryGame);
    }
    
    public void _deleteAllSubjectNotUsed(@NonNull MemoryGameEntity memoryGame, Set<String> subjectNameSet) {
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
                               .filter(SubjectEntity::noMemoryGame)
                               .collect(Collectors.toSet());
        
        subjectRepository.deleteAll(subjectSet);
    }
    
    public void _deleteAllSubjectNotUsed(MemoryGameEntity memoryGame) {
        _deleteAllSubjectNotUsed(memoryGame, null);
    }
}