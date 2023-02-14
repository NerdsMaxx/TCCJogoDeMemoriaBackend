package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.GameplayRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.PlayerGameplayFinsishedResponseDto;
import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.application.mappers.GameplayMapper;
import com.tcc.app.web.memory_game.api.application.services.GameplayService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    
    @PostMapping("/terminar/{code}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity finishGameplay(@NotBlank @PathVariable("code") String code,
                                         @RequestBody @Valid PlayerScoreRequestDto playerScoreRequestDto) throws Exception{
        Object[] resultList = gameplayService.finishGameplayByCode(code, playerScoreRequestDto);
        
        return ResponseEntity.ok(gameplayMapper.toPlayerGameplayFinsishedResponseDto((PlayerGameplayEntity) resultList[0],
                                                                                     (CodeGameplayEntity) resultList[1]));
    }
}