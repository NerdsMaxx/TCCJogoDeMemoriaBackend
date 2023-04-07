package com.tcc.app.web.memory_game.api.services;

import com.tcc.app.web.memory_game.api.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.repositories.CodeGameplayRepository;
import com.tcc.app.web.memory_game.api.repositories.GameplayRepository;
import com.tcc.app.web.memory_game.api.repositories.PlayerGameplayRepository;
import com.tcc.app.web.memory_game.api.utils.GameplayUtilStatic;
import com.tcc.app.web.memory_game.api.custom.CustomException;
import com.tcc.app.web.memory_game.api.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.custom.Quartet;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class GameplayService {
    
    @Autowired
    private GameplayRepository gameplayRepository;
    
    @Autowired
    private CodeGameplayRepository codeGameplayRepository;
    
    @Autowired
    private PlayerGameplayRepository playerGameplayRepository;
    
    @Autowired
    private MemoryGameService memoryGameService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GameplayMapper gameplayMapper;
    
    public List<CodeGameplayEntity> findAllCodeGameplay() {
        return codeGameplayRepository.findAll();
    }
    
    public void deleteAllCodeGameplay(List<CodeGameplayEntity> codeGameplayList) {
        codeGameplayRepository.deleteAll(codeGameplayList);
    }
    
    public CodeGameplayEntity generateGameplay(GameplayRequestDto gameplayRequestDto) throws Exception {
        UserEntity creator = userService.getCurrentUser();
        
        if(! creator.isCreator()) {
            creator = userService.findCreatorByUsernameOrEmail(gameplayRequestDto.creator());
        }
        
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(creator,
                                                                                   gameplayRequestDto.memoryGame());
        
        GameplayEntity gameplay = new GameplayEntity(memoryGame);
        CodeGameplayEntity codeGameplay = new CodeGameplayEntity(GameplayUtilStatic.generateCode(), gameplay);
        
        Boolean alone = gameplayRequestDto.alone();
        if (alone != null && alone) {
            gameplay.setAlone(true);
//            _addPlayerInGameplay(authenticatedUserUtil.getCurrentPlayer(), gameplay, codeGameplay);
//            return codeGameplay;
        }
        
        gameplay.setCodeGameplay(codeGameplay);
        
        gameplayRepository.save(gameplay);

        return codeGameplay;
    }
    
    public PlayerGameplayEntity addPlayerAndGameplayByCode(String username, String code) throws Exception {
        UserEntity player = userService.findPlayerByUsernameOrEmail(username);
        return _addPlayerInGameplay(player, code);
    }
    
    public PlayerGameplayEntity addGameplayByCode(String code) throws Exception {
        UserEntity player = userService.getCurrentPlayer();
        return _addPlayerInGameplay(player, code);
    }
    
    public Quartet<Set<PlayerGameplayEntity>, CodeGameplayEntity, String, String>
    finishGameplayByCode(String code, PlayerScoreRequestDto playerScoreRequestDto) throws Exception {
        CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
        
        UserEntity player = userService.getCurrentPlayer();
        PlayerGameplayEntity playerGameplay = _getPlayerGameplay(player, gameplay);
        playerGameplay = gameplayMapper.updatePlayerGameplay(playerScoreRequestDto, playerGameplay);

        codeGameplay.removeOnePlayer();
        
        gameplay.setCodeGameplay(codeGameplay)
                .updatePlayerGameplay(playerGameplay);
        
        gameplayRepository.save(gameplay);
        
        String memoryGameName = gameplay.getMemoryGame().getMemoryGame();
        String creatorName = gameplay.getMemoryGame().getCreator().getUsername();
        
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGameName,
                             creatorName);
    }
    
    public Quartet<Set<PlayerGameplayEntity>, CodeGameplayEntity, String, String> getGameplayScoresByCode(String code) throws Exception {
        CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
    
        String memoryGameName = gameplay.getMemoryGame().getMemoryGame();
        String creatorUsername = gameplay.getMemoryGame().getCreator().getUsername();
    
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGameName,
                             creatorUsername);
    }
    
    public Set<CodeGameplayEntity> getCodeSet() throws Exception {
        UserEntity creator = userService.getCurrentCreator();
        
        return codeGameplayRepository.findCodeSetByCreator(creator);
    }
    
    public List<PlayerGameplayEntity> getPreviousGameplays() throws Exception {
        UserEntity player = userService.getCurrentPlayer();
        
        return playerGameplayRepository.findAllByPlayer(player);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(UserEntity player, String code) throws Exception {
        CodeGameplayEntity codeGameplay = _getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
        
        return _addPlayerInGameplay(player, gameplay, codeGameplay);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(UserEntity player, GameplayEntity gameplay, CodeGameplayEntity codeGameplay) throws Exception {
//        if (playerGameplayRepository.existsByPlayerAndGameplay(player, gameplay)) {
//            throw new EntityExistsException("O jogador já foi adicionado no gameplay!");
//        }
        
        PlayerGameplayEntity playerGameplay = playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                                                      .orElse(new PlayerGameplayEntity(player, gameplay));
        if(playerGameplay.getId() != null) {
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
    
    private CodeGameplayEntity _getCodeGameplay(String code) throws Exception {
        return codeGameplayRepository.findByCode(code)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "Não foi criado um gameplay ou código foi expirado!"));
    }
    
    private PlayerGameplayEntity _getPlayerGameplay(UserEntity player, GameplayEntity gameplay) throws Exception {
        return playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               "O jogador não foi encontrado para este gameplay!"));
    }
    
}