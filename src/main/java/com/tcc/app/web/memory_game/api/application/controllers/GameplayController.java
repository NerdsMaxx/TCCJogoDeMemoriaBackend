package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.application.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.application.services.GameplayService;
import com.tcc.app.web.memory_game.api.custom.Pair;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gameplay")
@CrossOrigin("*")
public class GameplayController {
    
    @Autowired
    private GameplayService gameplayService;
    
    @Autowired
    private GameplayMapper gameplayMapper;
    
    @PostMapping("/comecar")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity generateGameplay(@RequestBody @Valid GameplayRequestDto gameplayRequestDto) throws Exception {
        CodeGameplayEntity codeGameplay = gameplayService.generateGameplay(gameplayRequestDto);
        
        return ResponseEntity.ok(gameplayMapper.toGameplayResponseDto(codeGameplay));
    }
    
    @PostMapping("/jogar/{code}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity addPlayerInGameplay(@PathVariable("code") String code) throws Exception {
        PlayerGameplayEntity playerGameplayEntity = gameplayService.addGameplayByCode(code);
        
        return ResponseEntity.ok(gameplayMapper.toPlayerAddedResponseDto(playerGameplayEntity));
    }
    
    @PostMapping("/terminar/{code}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity finishGameplay(@NotBlank @PathVariable("code") String code,
                                         @RequestBody
                                         @Valid PlayerScoreRequestDto playerScoreRequestDto) throws Exception {
        var result = gameplayService.finishGameplayByCode(code, playerScoreRequestDto);
        
        return ResponseEntity.ok(gameplayMapper.toGameplayResultDto(result.v1(),
                                                                    result.v2(),
                                                                    result.v3(),
                                                                    result.v4()));
    }
    
    
    @GetMapping("/pontuacoes/{code}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getGameplayScores(@NotBlank @PathVariable("code") String code) throws Exception {
        var result = gameplayService.getGameplayScoresByCode(code);
    
        return ResponseEntity.ok(gameplayMapper.toGameplayResultDto(result.v1(),
                                                                    result.v2(),
                                                                    result.v3(),
                                                                    result.v4()));
    }
    
    @GetMapping("/codigos")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getGameplayScores() throws Exception {
        Set<CodeGameplayEntity> codeGameplaySet = gameplayService.getCodeSet();
        
        return ResponseEntity.ok(gameplayMapper.toCodesResponseDto(codeGameplaySet));
    }
    
    @GetMapping("/partidas-anteriores")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getPreviousGameplays() throws Exception {
        Set<PlayerGameplayEntity> playerGameplaySet = gameplayService.getPreviousGameplays();
        
        return ResponseEntity.ok(playerGameplaySet.stream()
                                                  .map(gameplayMapper::toPreviousGameplaysResponseDTO)
                                                  .collect(Collectors.toSet()));
    }
}