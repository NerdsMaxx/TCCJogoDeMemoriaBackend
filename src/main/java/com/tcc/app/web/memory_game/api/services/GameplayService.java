package com.tcc.app.web.memory_game.api.services;

import com.tcc.app.web.memory_game.api.custom.Quartet;
import com.tcc.app.web.memory_game.api.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.entities.*;
import com.tcc.app.web.memory_game.api.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.repositories.CodeGameplayRepository;
import com.tcc.app.web.memory_game.api.repositories.GameplayRepository;
import com.tcc.app.web.memory_game.api.repositories.PlayerGameplayRepository;
import com.tcc.app.web.memory_game.api.utils.GameplayUtilStatic;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    
    public CodeGameplayEntity generateGameplay(@NonNull GameplayRequestDto gameplayRequestDto) throws Exception {
        UserEntity creator = userService.getCurrentUser();
        
        if (! creator.isCreator()) {
            creator = userService.findCreatorByUsernameOrEmail(gameplayRequestDto.creator());
        }
        
        final MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(creator,
                                                                                         gameplayRequestDto.memoryGame());
        
        final Boolean alone = gameplayRequestDto.alone();
        final GameplayEntity gameplay = (alone != null && alone) ?
                                        new GameplayEntity(true, memoryGame) :
                                        new GameplayEntity(false, memoryGame);
        
        final CodeGameplayEntity codeGameplay = new CodeGameplayEntity(GameplayUtilStatic.generateCode(), gameplay);
        
        gameplay.setCodeGameplay(codeGameplay);
        
        gameplayRepository.save(gameplay);
        
        return codeGameplay;
    }
    
    public PlayerGameplayEntity addPlayerAndGameplayByCode(@NonNull String username, @NonNull String code) throws Exception {
        final UserEntity player = userService.findPlayerByUsernameOrEmail(username);
        return _addPlayerInGameplay(player, code);
    }
    
    public PlayerGameplayEntity addGameplayByCode(@NonNull String code) throws Exception {
        final UserEntity player = userService.getCurrentPlayer();
        return _addPlayerInGameplay(player, code);
    }
    
    public Quartet<Set<PlayerGameplayEntity>,CodeGameplayEntity,String,String>
    finishGameplayByCode(@NonNull String code, @NonNull PlayerScoreRequestDto playerScoreRequestDto) throws Exception {
        final CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        final GameplayEntity gameplay = codeGameplay.getGameplay();
        
        final UserEntity player = userService.getCurrentPlayer();
        PlayerGameplayEntity playerGameplay = _getPlayerGameplay(player, gameplay);
        playerGameplay = gameplayMapper.updatePlayerGameplay(playerScoreRequestDto, playerGameplay);
        
        codeGameplay.removeOnePlayer();
        
        gameplay.setCodeGameplay(codeGameplay)
                .updatePlayerGameplay(playerGameplay);
        
        gameplayRepository.save(gameplay);
        
        final String memoryGameName = gameplay.getMemoryGame().getMemoryGame();
        final String creatorName = gameplay.getMemoryGame().getCreator().getUsername();
        
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGameName,
                             creatorName);
    }
    
    public Quartet<Set<PlayerGameplayEntity>,CodeGameplayEntity,String,String>
    getGameplayScoresByCode(@NonNull String code) throws Exception {
        final CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        final GameplayEntity gameplay = codeGameplay.getGameplay();
        
        final String memoryGameName = gameplay.getMemoryGame().getMemoryGame();
        final String creatorUsername = gameplay.getMemoryGame().getCreator().getUsername();
        
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGameName,
                             creatorUsername);
    }
    
    public Set<CodeGameplayEntity> getCodeSet() throws Exception {
        final UserEntity creator = userService.getCurrentCreator();
        
        return codeGameplayRepository.findCodeSetByCreator(creator);
    }
    
    public List<PlayerGameplayEntity> getPreviousGameplays() throws Exception {
        final UserEntity player = userService.getCurrentPlayer();
        
        return playerGameplayRepository.findAllByPlayer(player);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(@NonNull UserEntity player, @NonNull String code) throws Exception {
        final CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        final GameplayEntity gameplay = codeGameplay.getGameplay();
        
        return _addPlayerInGameplay(player, gameplay, codeGameplay);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(@NonNull UserEntity player, @NonNull GameplayEntity gameplay, @NonNull CodeGameplayEntity codeGameplay) throws Exception {
//        if (playerGameplayRepository.existsByPlayerAndGameplay(player, gameplay)) {
//            throw new EntityExistsException("O jogador já foi adicionado no gameplay!");
//        }
        
        final var isFirst = new Object() {
            boolean value = false;
        };
        final PlayerGameplayEntity playerGameplay;
        playerGameplay = playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                                 .orElseGet(() -> {
                                                     isFirst.value = true;
                                                     return new PlayerGameplayEntity(player, gameplay);
                                                 });
        
        if (isFirst.value) {
            return playerGameplay;
        }
        
        //memoryGameService.addPlayer(gameplay.getMemoryGame(), player);
        
        //PlayerGameplayEntity playerGameplay = new PlayerGameplayEntity(player, gameplay);
        codeGameplay.sumOnePlayer();
        
        gameplay.sumOnePlayer()
                .setCodeGameplay(codeGameplay)
                .addPlayerGameplay(playerGameplay);
        
        gameplayRepository.save(gameplay);
        
        return playerGameplay;
    }
    
    private CodeGameplayEntity _getCodeGameplay(@NonNull String code) throws Exception {
        return codeGameplayRepository.findByCode(code)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "Não foi criado um gameplay ou código foi expirado!"));
    }
    
    private PlayerGameplayEntity _getPlayerGameplay(@NonNull UserEntity player, @NonNull GameplayEntity gameplay) throws Exception {
        return playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               "O jogador não foi encontrado para este gameplay!"));
    }
    
}