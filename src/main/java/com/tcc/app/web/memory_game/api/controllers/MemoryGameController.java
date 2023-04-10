package com.tcc.app.web.memory_game.api.controllers;

import com.tcc.app.web.memory_game.api.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.mappers.MemoryGameMapper;
import com.tcc.app.web.memory_game.api.services.MemoryGameService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.NoPermissionException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jogo-de-memoria")
@CrossOrigin("*")
@AllArgsConstructor
public class MemoryGameController {
    
    private final MemoryGameService memoryGameService;
    private final MemoryGameMapper memoryGameMapper;
    
    @GetMapping("/criador")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getAllMemoryGameForCreator() {
        Set<MemoryGameEntity> memoryGameSet = memoryGameService.findAll(Set.of(UserTypeEnum.CRIADOR));
        
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @GetMapping("/jogador")
    @PreAuthorize("hasRole('ROLE_JOGADOR')")
    public ResponseEntity getAllMemoryGameForPlayer() {
        Set<MemoryGameEntity> memoryGameSet = memoryGameService.findAll(Set.of(UserTypeEnum.JOGADOR));
        
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @GetMapping("/{memoryGameName}")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity getCardsByCreatorAndMemoryGame(
            @PathVariable("memoryGameName") String memoryGameName) throws NoPermissionException {
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(memoryGameName);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameCardsResponseDto(memoryGame));
    }
    
    @GetMapping("/{memoryGameName}/{creatorUsername}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getCardsByCreatorAndMemoryGame(
            @PathVariable("memoryGameName") String memoryGameName,
            @PathVariable("creatorUsername") String creatorUsername) {
        MemoryGameEntity memoryGame = memoryGameService.findByCreatorAndMemoryGame(memoryGameName, creatorUsername);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameCardsResponseDto(memoryGame));
    }
    
    @GetMapping("/pesquisar/criador/{search}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getMemoryGamesByMemoryGameNameAndSubjectForCreator(
            @PathVariable("search") String search) {
        Set<MemoryGameEntity> memoryGameSet;
        memoryGameSet = memoryGameService.findByMemoryGameNameAndSubject(search, Set.of(UserTypeEnum.CRIADOR));
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @GetMapping("/pesquisar/jogador/{search}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or hasRole('ROLE_JOGADOR')")
    public ResponseEntity getMemoryGamesByMemoryGameNameAndSubjectForPlayer(
            @PathVariable("search") String search) {
        Set<MemoryGameEntity> memoryGameSet;
        memoryGameSet = memoryGameService.findByMemoryGameNameAndSubject(search, Set.of(UserTypeEnum.JOGADOR));
        
        return ResponseEntity.ok(memoryGameSet.stream()
                                              .map(memoryGameMapper::toMemoryGameResponseDto)
                                              .collect(Collectors.toSet()));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity saveMemoryGame(
            @RequestBody @Valid MemoryGameRequestDto memoryGameRequestDto,
            UriComponentsBuilder uriBuilder) throws NoPermissionException {
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
                                           @RequestBody MemoryGameRequestDto memoryGameRequestDto) throws NoPermissionException {
        MemoryGameEntity memoryGame = memoryGameService.update(memoryGameName, memoryGameRequestDto);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameResponseDto(memoryGame));
    }
    
    @DeleteMapping("/{memoryGame}")
    @PreAuthorize("hasRole('ROLE_CRIADOR')")
    public ResponseEntity deleteMemoryGame(@PathVariable(value = "memoryGame") String memoryGameName) throws NoPermissionException {
        memoryGameService.delete(memoryGameName);
        
        return ResponseEntity.ok("Jogo de memória deletado com sucesso!");
    }
}