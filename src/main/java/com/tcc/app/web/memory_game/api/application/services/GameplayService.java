package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.*;
import com.tcc.app.web.memory_game.api.application.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.application.repositories.CodeGameplayRepository;
import com.tcc.app.web.memory_game.api.application.repositories.GameplayRepository;
import com.tcc.app.web.memory_game.api.application.repositories.PlayerGameplayRepository;
import com.tcc.app.web.memory_game.api.application.utils.GameplayUtil;
import com.tcc.app.web.memory_game.api.custom.CustomException;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import com.tcc.app.web.memory_game.api.custom.Quartet;
import jakarta.persistence.EntityExistsException;
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
    private GameplayUtil gameplayUtil;
    
    @Autowired
    private AuthenticatedUserUtil authenticatedUserUtil;
    
    @Autowired
    private GameplayMapper gameplayMapper;
    
    public List<CodeGameplayEntity> findAllCodeGameplay() {
        return codeGameplayRepository.findAll();
    }
    
    public void deleteAllCodeGameplay(List<CodeGameplayEntity> codeGameplayList) {
        codeGameplayRepository.deleteAll(codeGameplayList);
    }
    
    public CodeGameplayEntity generateGameplay(GameplayRequestDto gameplayRequestDto) throws Exception {
        UserEntity creator = (authenticatedUserUtil.isCreator()) ?
                             authenticatedUserUtil.getCurrentCreator() :
                             userService.findCreatorByUsernameOrEmail(gameplayRequestDto.creator())
                                        .orElseThrow();
        
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(creator,
                                                                                   gameplayRequestDto.memoryGame());
        
        GameplayEntity gameplay = new GameplayEntity(memoryGame);
        CodeGameplayEntity codeGameplay = new CodeGameplayEntity(gameplayUtil.generateCode(), gameplay);
        
        if (Objects.requireNonNullElse(gameplayRequestDto.alone(), false)) {
            gameplay.setAlone(true);
//            _addPlayerInGameplay(authenticatedUserUtil.getCurrentPlayer(), gameplay, codeGameplay);
//            return codeGameplay;
        }
        
        gameplay.setCodeGameplay(codeGameplay);
        
        gameplayRepository.save(gameplay);

        return codeGameplay;
    }
    
    public PlayerGameplayEntity addPlayerAndGameplayByCode(String username, String code) throws Exception {
        Optional<UserEntity> player = userService.findPlayerByUsernameOrEmail(username);
        return _addPlayerInGameplay(player.orElseThrow(() -> new CustomException("Não foi encontrado usuário jogador.")), code);
    }
    
    public PlayerGameplayEntity addGameplayByCode(String code) throws Exception {
        UserEntity player = authenticatedUserUtil.getCurrentPlayer();
        return _addPlayerInGameplay(player, code);
    }
    
    public Quartet<Set<PlayerGameplayEntity>, CodeGameplayEntity, String, String>
    finishGameplayByCode(String code, PlayerScoreRequestDto playerScoreRequestDto) throws Exception {
        CodeGameplayEntity codeGameplay = gameplayUtil.getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
        
        UserEntity player = authenticatedUserUtil.getCurrentPlayer();
        PlayerGameplayEntity playerGameplay = gameplayUtil.getPlayerGameplay(player, gameplay);
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
        CodeGameplayEntity codeGameplay = gameplayUtil.getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
    
        String memoryGameName = gameplay.getMemoryGame().getMemoryGame();
        String creatorUsername = gameplay.getMemoryGame().getCreator().getUsername();
    
        return new Quartet<>(gameplay.getPlayerGameplaySet(),
                             codeGameplay,
                             memoryGameName,
                             creatorUsername);
    }
    
    public Set<CodeGameplayEntity> getCodeSet() throws Exception {
        UserEntity creator = authenticatedUserUtil.getCurrentCreator();
        
        return codeGameplayRepository.findCodeSetByCreator(creator);
    }
    
    public Set<PlayerGameplayEntity> getPreviousGameplays() throws Exception {
        UserEntity player = authenticatedUserUtil.getCurrentPlayer();
        
        return playerGameplayRepository.findAllByPlayer(player);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(UserEntity player, String code) throws Exception {
        CodeGameplayEntity codeGameplay = gameplayUtil.getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
        
        return _addPlayerInGameplay(player, gameplay, codeGameplay);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(UserEntity player, GameplayEntity gameplay, CodeGameplayEntity codeGameplay) throws Exception {
        if (playerGameplayRepository.existsByPlayerAndGameplay(player, gameplay)) {
            throw new EntityExistsException("O jogador já foi adicionado no gameplay!");
        }
        
        memoryGameService.addPlayer(gameplay.getMemoryGame(), player);
        
        PlayerGameplayEntity playerGameplay = new PlayerGameplayEntity(player, gameplay);
        codeGameplay.sumOnePlayer();
        
        gameplay.sumOnePlayer()
                .setCodeGameplay(codeGameplay)
                .addPlayerGameplay(playerGameplay);
        
        gameplayRepository.save(gameplay);
        
        return playerGameplay;
    }
    
    
}