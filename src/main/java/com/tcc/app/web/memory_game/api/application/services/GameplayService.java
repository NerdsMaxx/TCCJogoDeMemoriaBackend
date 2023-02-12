package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.*;
import com.tcc.app.web.memory_game.api.application.repositories.CodeGameplayRepository;
import com.tcc.app.web.memory_game.api.application.repositories.GameplayRepository;
import com.tcc.app.web.memory_game.api.application.repositories.PlayerGameplayRepository;
import com.tcc.app.web.memory_game.api.application.utils.GameplayUtil;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private PlayerService playerService;
    
    @Autowired
    private CreatorService creatorService;
    
    @Autowired
    private GameplayUtil gameplayUtil;
    
    @Autowired
    private AuthenticatedUserUtil authenticatedUserUtil;
    
    public List<CodeGameplayEntity> findAllCodeGameplay() {
        return codeGameplayRepository.findAll();
    }
    
    public void deleteAllCodeGameplay(List<CodeGameplayEntity> codeGameplayList) {
        codeGameplayRepository.deleteAll(codeGameplayList);
    }
    
    public CodeGameplayEntity generateGameplay(GameplayRequestDto gameplayRequestDto) throws Exception {
        CreatorEntity creator = (authenticatedUserUtil.isCreator()) ?
                                authenticatedUserUtil.getCurrentCreator() :
                                creatorService.findByUsername(gameplayRequestDto.creator());
        
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(creator,
                                                                                   gameplayRequestDto.memoryGame());
        
        GameplayEntity gameplay = new GameplayEntity(memoryGame);
        CodeGameplayEntity codeGameplay = new CodeGameplayEntity(gameplayUtil.generateCode(), gameplay);
        
        if (authenticatedUserUtil.isPlayer()) {
            _addPlayerInGameplay(authenticatedUserUtil.getCurrentPlayer(), gameplay, codeGameplay);
            return codeGameplay;
        }
        
        gameplayRepository.save(gameplay);
        codeGameplayRepository.save(codeGameplay);
        return codeGameplay;
    }
    
    public PlayerGameplayEntity addPlayerAndGameplayByCode(String username, String code) throws Exception {
        PlayerEntity player = playerService.findByUsername(username);
        return _addPlayerInGameplay(player, code);
    }
    
    public PlayerGameplayEntity addGameplayByCode(String code) throws Exception {
        PlayerEntity player = authenticatedUserUtil.getCurrentPlayer();
        return _addPlayerInGameplay(player, code);
    }
    
    public Object[] finishGameplayByCode(String code, PlayerScoreRequestDto playerScoreRequestDto) throws Exception {
        CodeGameplayEntity codeGameplay = gameplayUtil.getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
        
        PlayerEntity player = authenticatedUserUtil.getCurrentPlayer();
        PlayerGameplayEntity playerGameplay = gameplayUtil.getPlayerGameplay(player, gameplay);
        
        codeGameplay.removeOnePlayer();
        codeGameplayRepository.save(codeGameplay);
        
        playerGameplay.setScores(playerScoreRequestDto);
        playerGameplayRepository.save(playerGameplay);
        
        return new Object[]{playerGameplay, codeGameplay};
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(PlayerEntity player, String code) throws Exception {
        CodeGameplayEntity codeGameplay = gameplayUtil.getCodeGameplay(code);
        GameplayEntity gameplay = codeGameplay.getGameplay();
        
        return _addPlayerInGameplay(player, gameplay, codeGameplay);
    }
    
    private PlayerGameplayEntity _addPlayerInGameplay(PlayerEntity player, GameplayEntity gameplay, CodeGameplayEntity codeGameplay) throws Exception {
        if (playerGameplayRepository.existsByPlayerAndGameplay(player, gameplay)) {
            throw new EntityExistsException("O jogador já foi adicionado no gameplay!");
        }
        
        playerService.addMemoryGame(player, gameplay.getMemoryGame());
        
        gameplay.sumOnePlayer();
        gameplayRepository.save(gameplay);
        
        codeGameplay.sumOnePlayer();
        codeGameplayRepository.save(codeGameplay);
        
        return playerGameplayRepository.save(new PlayerGameplayEntity(player, gameplay));
    }
}