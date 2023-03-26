package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerMemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.MemoryGameMapper;
import com.tcc.app.web.memory_game.api.application.services.MemoryGameService;
import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jogo-de-memoria")
@CrossOrigin("*")
public class MemoryGameController {
    
    @Autowired
    private MemoryGameService memoryGameService;
    
    @Autowired
    private MemoryGameMapper memoryGameMapper;
    
    @GetMapping("/criador")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getAllMemoryGameForCreator() throws Exception {
        Set<MemoryGameEntity> memoryGameSet = memoryGameService.findAll(Set.of(UserTypeEnum.CRIADOR));
        
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @GetMapping("/jogador")
    @PreAuthorize("hasRole('ROLE_JOGADOR')")
    public ResponseEntity getAllMemoryGameForPlayer() throws Exception {
        Set<MemoryGameEntity> memoryGameSet = memoryGameService.findAll(Set.of(UserTypeEnum.JOGADOR));
        
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @GetMapping("/{memoryGameName}")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getCardsByCreatorAndMemoryGame(
            @PathVariable("memoryGameName") String memoryGameName) throws Exception {
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(memoryGameName);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameCardsResponseDto(memoryGame));
    }
    
    @GetMapping("/{memoryGameName}/{creatorUsername}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getCardsByCreatorAndMemoryGame(
            @PathVariable("memoryGameName") String memoryGameName,
            @PathVariable("creatorUsername") String creatorUsername) throws Exception {
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(memoryGameName, creatorUsername);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameCardsResponseDto(memoryGame));
    }
    
    @GetMapping("/pesquisar/criador/{search}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getMemoryGamesByMemoryGameNameAndSubjectForCreator(
            @PathVariable("search") String search) throws Exception {
        Set<MemoryGameEntity> memoryGameSet;
        memoryGameSet = memoryGameService.findByMemoryGameNameAndSubject(search, Set.of(UserTypeEnum.CRIADOR));
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @GetMapping("/pesquisar/jogador/{search}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getMemoryGamesByMemoryGameNameAndSubjectForPlayer(
            @PathVariable("search") String search) throws Exception {
        Set<MemoryGameEntity> memoryGameSet;
        memoryGameSet = memoryGameService.findByMemoryGameNameAndSubject(search, Set.of(UserTypeEnum.JOGADOR));
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @PostMapping("/jogador")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity addPlayerInMemoryGame(
            @RequestBody @Valid PlayerMemoryGameRequestDto playerMemoryGameRequestDto) throws Exception {
        String result = memoryGameService.addPlayer(playerMemoryGameRequestDto);
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity saveMemoryGame(
            @RequestBody @Valid MemoryGameRequestDto memoryGameRequestDto,
            UriComponentsBuilder uriBuilder) throws Exception {
        MemoryGameEntity memoryGame = memoryGameService.save(memoryGameRequestDto);
        
        var uri = uriBuilder.path("/jogo-de-memoria/{id}")
                            .buildAndExpand(memoryGame.getId())
                            .toUri();
        
        return ResponseEntity.created(uri)
                             .body(memoryGameMapper.toMemoryGameResponseDto(memoryGame));
    }
    
    @PutMapping("/{memoryGameName}")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity updateMemoryGame(@PathVariable(value = "memoryGameName") String memoryGameName,
                                           @RequestBody MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        MemoryGameEntity memoryGame = memoryGameService.update(memoryGameName, memoryGameRequestDto);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameResponseDto(memoryGame));
    }
    
    @DeleteMapping("/{memoryGame}")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity deleteMemoryGame(@PathVariable(value = "memoryGame") String memoryGameName) throws Exception {
        memoryGameService.delete(memoryGameName);
        
        return ResponseEntity.ok("Jogo de memória deletado com sucesso!");
    }
}