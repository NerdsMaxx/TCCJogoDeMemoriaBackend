package com.tcc.app.web.memory_game.api.services;

import com.tcc.app.web.memory_game.api.custom.Quartet;
import com.tcc.app.web.memory_game.api.custom.SimpleValue;
import com.tcc.app.web.memory_game.api.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.entities.*;
import com.tcc.app.web.memory_game.api.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.repositories.CodeGameplayRepository;
import com.tcc.app.web.memory_game.api.repositories.GameplayRepository;
import com.tcc.app.web.memory_game.api.repositories.PlayerGameplayRepository;
import com.tcc.app.web.memory_game.api.utils.CollectionUtil;
import com.tcc.app.web.memory_game.api.utils.GameplayUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class GameplayService {
    private final GameplayRepository gameplayRepository;
    private final CodeGameplayRepository codeGameplayRepository;
    private final PlayerGameplayRepository playerGameplayRepository;
    private final MemoryGameService memoryGameService;
    private final UserService userService;
    private final GameplayMapper gameplayMapper;
    
    public List<CodeGameplayEntity> findAllCodeGameplay() {
        return codeGameplayRepository.findAll();
    }
    
    public void deleteAllCodeInvalidated() {
        codeGameplayRepository.deleteAllInvalidated();
    }
    
    public CodeGameplayEntity generateGameplay(@NonNull final GameplayRequestDto gameplayRequestDto) {
        UserEntity creator = userService.getCurrentUser();
        final String usernameOrEmailCreator = gameplayRequestDto.creator();
        
        if (creator.isNotCreator() || creator.notEqualsUsernameOrEmail(usernameOrEmailCreator)) {
            creator = userService.findCreatorByUsernameOrEmail(usernameOrEmailCreator);
        }
        
        final MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(creator,
                                                                                         gameplayRequestDto.memoryGame());
        
        final Boolean alone = gameplayRequestDto.alone();
        final GameplayEntity gameplay = (alone != null && alone) ?
                                        new GameplayEntity(true, memoryGame) :
                                        new GameplayEntity(false, memoryGame);
        
        final CodeGameplayEntity codeGameplay = new CodeGameplayEntity(GameplayUtil.generateCode(), gameplay);
        
        gameplay.setCodeGameplay(codeGameplay);
        
        gameplayRepository.save(gameplay);
        
        return codeGameplay;
    }
    
    public PlayerGameplayEntity addPlayerAndGameplayByCode(@NonNull String username, @NonNull String code) {
        final UserEntity player = userService.findPlayerByUsernameOrEmail(username);
        return _addPlayerInGameplay(player, code);
    }
    
    public PlayerGameplayEntity addGameplayByCode(@NonNull String code) throws NoPermissionException {
        final UserEntity player = userService.getCurrentPlayer();
        return _addPlayerInGameplay(player, code);
    }
    
    public Quartet<Set<PlayerGameplayEntity>,CodeGameplayEntity,String,String>
    finishGameplayByCode(@NonNull String code, @NonNull PlayerScoreRequestDto playerScoreRequestDto) throws NoPermissionException {
        final CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        final GameplayEntity gameplay = codeGameplay.getGameplay();
        
        final UserEntity player = userService.getCurrentPlayer();
        
        final PlayerGameplayEntity playerGameplay = _getPlayerGameplay(player, gameplay);
        gameplayMapper.updatePlayerGameplay(playerScoreRequestDto, playerGameplay);
        playerGameplay.finishGameplay();
        
        codeGameplay.removeOnePlayer();
        
        gameplay.setCodeGameplay(codeGameplay)
                .updatePlayerGameplay(playerGameplay);
        
        gameplayRepository.save(gameplay);
        
        final MemoryGameEntity memoryGame = gameplay.getMemoryGame();
        
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGame.getMemoryGame(),
                             memoryGame.getCreator().getUsername());
    }
    
    public Quartet<Set<PlayerGameplayEntity>,CodeGameplayEntity,String,String>
    getGameplayScoresByCode(@NonNull String code) {
        final CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        final GameplayEntity gameplay = codeGameplay.getGameplay();
        final MemoryGameEntity memoryGame = gameplay.getMemoryGame();
        
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGame.getMemoryGame(),
                             memoryGame.getCreator().getUsername());
    }
    
    public Set<CodeGameplayEntity> getCodeSet() throws NoPermissionException {
        final UserEntity creator = userService.getCurrentCreator();
        
        return codeGameplayRepository.findCodeSetByCreator(creator);
    }
    
    public Set<PlayerGameplayEntity> getPreviousGameplays() throws NoPermissionException {
        final UserEntity player = userService.getCurrentPlayer();
        
        return playerGameplayRepository.findAllByPlayer(player);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(@NonNull UserEntity player, @NonNull String code) {
        final CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        final GameplayEntity gameplay = codeGameplay.getGameplay();
        
        return _addPlayerInGameplay(player, gameplay, codeGameplay);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(@NonNull UserEntity player,
                                                      @NonNull GameplayEntity gameplay,
                                                      @NonNull CodeGameplayEntity codeGameplay) {
        final SimpleValue<Boolean> isNotFirst = new SimpleValue<>(true);
        final PlayerGameplayEntity playerGameplay;
        playerGameplay = playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                                 .orElseGet(() -> {
                                                     isNotFirst.setValue(false);
                                                     return new PlayerGameplayEntity(player, gameplay);
                                                 });
        
        if (isNotFirst.getValue()) {
            return playerGameplay;
        }
        
        codeGameplay.sumOnePlayer();
        
        gameplay.sumOnePlayer()
                .setCodeGameplay(codeGameplay)
                .addPlayerGameplay(playerGameplay);
        
        gameplayRepository.save(gameplay);
        
        return playerGameplay;
    }
    
    private CodeGameplayEntity _getCodeGameplay(@NonNull String code) {
        return codeGameplayRepository.findByCode(code)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "Não foi criado um gameplay ou código foi expirado!"));
    }
    
    private PlayerGameplayEntity _getPlayerGameplay(@NonNull UserEntity player, @NonNull GameplayEntity gameplay) {
        return playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               "O jogador não foi encontrado para este gameplay!"));
    }
    
}