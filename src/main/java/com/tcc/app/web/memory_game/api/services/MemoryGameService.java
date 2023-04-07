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
import com.tcc.app.web.memory_game.api.utils.UserTypeUtilStatic;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public Set<MemoryGameEntity> findAll(@NonNull Set<UserTypeEnum> userTypeEnumSet) throws Exception {
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
                                                                @NonNull Set<UserTypeEnum> userTypeEnumSet) throws Exception {
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
                                                       @NonNull String memoryGameName) throws Exception {
        return memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException(MEMORY_GAME_NOT_FOUND));
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(@NonNull String memoryGameName) throws Exception {
        final UserEntity creator = userService.getCurrentCreator();
        return findByCreatorAndMemoryGame(creator, memoryGameName);
    }
    
    public MemoryGameEntity findByCreatorAndMemoryGame(@NonNull String memoryGameName,
                                                       @NonNull String creatorUsername) throws Exception {
        return memoryGameRepository.findByCreatorUsernameAndMemoryGame(creatorUsername, memoryGameName)
                                   .orElseThrow(() -> new EntityNotFoundException(MEMORY_GAME_NOT_FOUND));
    }
    
    public CardEntity findCardById(@NonNull Long id) throws Exception {
        return cardRepository.findById(id)
                             .orElseThrow(() -> new EntityNotFoundException("Não tem esta carta!"));
    }
    
    public MemoryGameEntity save(@NonNull MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        final UserEntity creator = userService.getCurrentCreator();
        
        if (memoryGameRepository.existsByCreatorAndMemoryGame(creator, memoryGameRequestDto.name())) {
            throw new EntityExistsException("Já existe o jogo de memória com este nome.");
        }
        
        final MemoryGameEntity memoryGame = new MemoryGameEntity(memoryGameRequestDto.name(), creator);
        
        final Set<CardEntity> newCardSet = memoryGameRequestDto.cardSet().stream()
                                                               .map(cardRequestDto -> cardMapper.toCardEntity(
                                                                       cardRequestDto, memoryGame))
                                                               .collect(Collectors.toSet());
        
        final Set<SubjectEntity> subjectFoundSet = subjectRepository.findBySubjectSet(
                memoryGameRequestDto.subjectSet());
        final Set<SubjectEntity> subjectSet = memoryGameRequestDto.subjectSet().stream()
                                                                  .map(SubjectEntity::new)
                                                                  .collect(Collectors.toSet());
        
        memoryGame.addCardSet(newCardSet)
                  .addSubjectList(subjectSet, subjectFoundSet);
        
        return memoryGameRepository.save(memoryGame);
    }
    
    public MemoryGameEntity update(@NonNull String memoryGameName, @NonNull MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        final UserEntity creator = userService.getCurrentCreator();
        
        final MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                        MEMORY_GAME_NOT_FOUND));
        
        final String name = memoryGameRequestDto.name();
        if (name != null) {
            memoryGame.setMemoryGame(name);
            memoryGameRepository.save(memoryGame);
        }

//        Set<CardEntity> cardSet = Optional.ofNullable(memoryGameRequestDto.cardSet())
//                                          .map(cardRequestDtoSet -> cardRequestDtoSet.stream()
//                                                                                     .map(cardMapper::toCardEntity)
//                                                                                     .collect(Collectors.toSet()))
//                                          .orElse(null);


//        Set<CardEntity> cardSet = Optional.ofNullable(memoryGameRequestDto.cardSet())
//                                          .orElse(new HashSet<>(0))
//                                          .stream().map(cardMapper::toCardEntity)
//                                          .collect(Collectors.toSet());
        
        final Set<CardRequestDto> cardRequestDtoSet = memoryGameRequestDto.cardSet();
        if (cardRequestDtoSet != null) {
            Set<CardEntity> cardSet = cardRequestDtoSet.stream()
                                                       .map(cardRequestDto -> cardMapper.toCardEntity(cardRequestDto,
                                                                                                      memoryGame))
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
    
    public void delete(@NonNull String memoryGameName) throws Exception {
        final UserEntity creator = userService.getCurrentCreator();
        
        final MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                        MEMORY_GAME_NOT_FOUND));
        
        _deleteAllSubjectNotUsed(memoryGame);
        
        memoryGame.clearCardSet();
        memoryGame.clearSubjectSet();
        memoryGameRepository.delete(memoryGame);
    }

//    public String addPlayer(PlayerMemoryGameRequestDto playerMemoryGameRequestDto) throws Exception {
//        UserEntity creator = userService.getCurrentCreator();
//
//        String memoryGameName = playerMemoryGameRequestDto.memoryGameName();
//        MemoryGameEntity memoryGame = memoryGameRepository.findByCreatorAndMemoryGame(creator, memoryGameName)
//                                                          .orElseThrow(() -> new EntityNotFoundException(
//                                                                  MEMORY_GAME_NOT_FOUND));
//
//        UserEntity player = userService.findPlayerByUsernameOrEmail(playerMemoryGameRequestDto.username());
//
//        memoryGame.addPlayer(player);
//        memoryGameRepository.save(memoryGame);
//
//        return "Jogador adicionado com sucesso!";
//    }

//    public void addPlayer(MemoryGameEntity memoryGame, UserEntity player) throws CustomException {
//        memoryGame.addPlayer(player);
//        memoryGameRepository.save(memoryGame);
//    }
    
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
                               .filter(subject -> subject.getMemoryGameSet().isEmpty())
                               .collect(Collectors.toSet());
        subjectRepository.deleteAll(subjectSet);
    }
    
    public void _deleteAllSubjectNotUsed(MemoryGameEntity memoryGame) {
        _deleteAllSubjectNotUsed(memoryGame, null);
    }
}