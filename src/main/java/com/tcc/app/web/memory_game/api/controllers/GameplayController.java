package com.tcc.app.web.memory_game.api.controllers;

import com.tcc.app.web.memory_game.api.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.services.GameplayService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.List;

@RestController
@RequestMapping("/gameplay")
@CrossOrigin("*")
@AllArgsConstructor
public class GameplayController {
    
    private final GameplayService gameplayService;
    private final GameplayMapper gameplayMapper;
    
    @PostMapping("/comecar")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity generateGameplay(@RequestBody @Valid GameplayRequestDto gameplayRequestDto) throws Exception {
        final CodeGameplayEntity codeGameplay = gameplayService.generateGameplay(gameplayRequestDto);
        
        return ResponseEntity.ok(gameplayMapper.toGameplayResponseDto(codeGameplay));
    }
    
    @PostMapping("/jogar/{code}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity addPlayerInGameplay(@PathVariable("code") String code) throws NoPermissionException {
        final PlayerGameplayEntity playerGameplayEntity = gameplayService.addGameplayByCode(code);
        
        return ResponseEntity.ok(gameplayMapper.toPlayerAddedResponseDto(playerGameplayEntity));
    }
    
    @PostMapping("/terminar/{code}")
    @PreAuthorize("hasRole('ROLE_JOGADOR')")
    public ResponseEntity finishGameplay(@NotBlank @PathVariable("code") String code,
                                         @Valid @RequestBody
                                         PlayerScoreRequestDto playerScoreRequestDto) throws NoPermissionException {
        final Long gameplayId = gameplayService.finishGameplayByCode(code, playerScoreRequestDto);
        
        return ResponseEntity.ok(gameplayId);
    }
    
    @GetMapping("/pontuacoes/jogador/{gameplayId}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getGameplayScoresByPlayerAndGameplayId(@NotBlank @PathVariable("gameplayId") Long gameplayId) throws NoPermissionException {
        final PlayerGameplayEntity playerGameplay = gameplayService.getScoresByPlayerAndGameplayId(gameplayId);
        
        return ResponseEntity.ok(gameplayMapper.toResultPlayerResponseDto(playerGameplay));
    }
    
    
    @GetMapping("/pontuacoes/criador/{code}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getGameplayScoresByCode(@NotBlank @PathVariable("code") String code) {
        final var result = gameplayService.getGameplayScoresByCode(code);
        
        return ResponseEntity.ok(gameplayMapper.toGameplayResultDto(result.v1(),
                                                                    result.v2(),
                                                                    result.v3(),
                                                                    result.v4()));
    }
    
    @GetMapping("/codigos")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getGameplayScores() throws NoPermissionException {
        final List<CodeGameplayEntity> codeGameplaySet = gameplayService.getCodeList();
        
        return ResponseEntity.ok(gameplayMapper.toCodesResponseDto(codeGameplaySet));
    }
    
    @GetMapping("/partidas-anteriores/jogador")
    @PreAuthorize("hasRole('ROLE_JOGADOR')")
    public ResponseEntity getPreviousGameplaysByPlayer() throws Exception {
        final List<PlayerGameplayEntity> playerGameplayList = gameplayService.getPreviousGameplaysByPlayer();
        
        return ResponseEntity.ok(playerGameplayList.stream()
                                                   .map(gameplayMapper::toPreviousGameplaysPlayerResponseDTO)
                                                   .toList());
    }
    
    @GetMapping("/partidas-anteriores/criador")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getPreviousGameplayByCreator() throws Exception {
        final List<GameplayEntity> gameplayList = gameplayService.getPreviousGameplaysByCreator();
        
        return ResponseEntity.ok(gameplayList.stream()
                                             .map(gameplayMapper::toPreviousGameplaysCreatorResponseDto)
                                             .toList());
    }
    
    @GetMapping("/partidas-anteriores/criador/jogadores/{id}")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getScoresByGameplay(@PathVariable("id") Long id) throws Exception {
        final List<PlayerGameplayEntity> playerGameplayList = gameplayService.getScoresByGameplayId(id);
        
        return ResponseEntity.ok(playerGameplayList.stream()
                                                   .map(gameplayMapper::toPreviousGameplaysPlayerResponseDTO)
                                                   .toList());
    }
}