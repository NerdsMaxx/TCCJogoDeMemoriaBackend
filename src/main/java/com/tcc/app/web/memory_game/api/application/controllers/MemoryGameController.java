package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.MemoryGameRequestDto;
import com.tcc.app.web.memory_game.api.application.mappers.MemoryGameMapper;
import com.tcc.app.web.memory_game.api.application.services.MemoryGameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getAllMemoryGamesByUser(Pageable pageable) throws Exception {
        var memoryGamePage = memoryGameService.findAllByUser(pageable)
                                              .map(memoryGameMapper::toMemoryGameResponseDto);
        
        return ResponseEntity.ok(memoryGamePage);
    }
    
    @PostMapping
    public ResponseEntity saveMemoryGame(
            @RequestBody @Valid MemoryGameRequestDto memoryGameRequestDto,
            UriComponentsBuilder uriBuilder) throws Exception {
        var memoryGame = memoryGameService.saveMemoryGame(memoryGameRequestDto);
        
        var uri = uriBuilder.path("/jogo-de-memoria/{id}")
                            .buildAndExpand(memoryGame.getId())
                            .toUri();
        
        return ResponseEntity.created(uri)
                             .body(memoryGameMapper.toMemoryGameResponseDto(memoryGame));
    }
    
    @PutMapping("/{memoryGame}")
    public ResponseEntity updateMemoryGame(@PathVariable(value = "memoryGame") String memoryGameName,
                                           @RequestBody MemoryGameRequestDto memoryGameRequestDto) throws Exception {
        var memoryGame = memoryGameService.updateMemoryGame(memoryGameName, memoryGameRequestDto);
        
        return ResponseEntity.ok(memoryGameMapper.toMemoryGameResponseDto(memoryGame));
    }
    
    @DeleteMapping("/{memoryGame}")
    public ResponseEntity deleteMemoryGame(@PathVariable(value = "memoryGame") String memoryGameName) throws Exception {
        memoryGameService.deleteMemoryGame(memoryGameName);
        
        return ResponseEntity.ok("Jogo de memória deletado com sucesso!");
    }
}