package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerMemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.mappers.MemoryGameMapper;
import com.tcc.app.web.memory_game.api.application.services.MemoryGameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/jogo-de-memoria")
@CrossOrigin("*")
public class MemoryGameController {
    
    @Autowired
    private MemoryGameService memoryGameService;
    
    @Autowired
    private MemoryGameMapper memoryGameMapper;
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getAllMemoryGame(Pageable pageable) throws Exception {
        var result = memoryGameService.findAll(pageable)
                                      .map(memoryGameMapper::toMemoryGameResponseDto);
        
        return ResponseEntity.ok(result);
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
    
    @GetMapping("/pesquisar/{search}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getMemoryGamesByMemoryGameNameAndSubject(
            @PathVariable("search") String search,
            Pageable pageable) throws Exception {
        Page<MemoryGameEntity> memoryGamePage = memoryGameService.findByMemoryGameNameAndSubject(pageable, search);
        
        return ResponseEntity.ok(memoryGamePage.map(memoryGameMapper::toMemoryGameResponseDto));
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