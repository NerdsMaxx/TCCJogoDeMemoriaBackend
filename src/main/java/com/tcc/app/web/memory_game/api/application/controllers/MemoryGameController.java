package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.dtos.requests.insert.MemoryGameInsertDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.update.MemoryGameUpdateDto;
import com.tcc.app.web.memory_game.api.application.mappers.MemoryGameMapper;
import com.tcc.app.web.memory_game.api.application.services.MemoryGameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping( "/jogo-de-memoria" )
@CrossOrigin( "*" )
public class MemoryGameController {
    
    @Autowired
    private MemoryGameService memoryGameService;
    
    @Autowired
    private MemoryGameMapper memoryGameMapper;
    
    @PostMapping
    public ResponseEntity insertNewMemoryGame(
            @RequestBody @Valid MemoryGameInsertDto memoryGameInsertDto,
            UriComponentsBuilder uriBuilder ) {
        var memoryGame = memoryGameService.registerNewMemoryGame( memoryGameInsertDto );
        
        var uri = uriBuilder.path( "/jogo-de-memoria/{id}" )
                            .buildAndExpand( memoryGame.getId() )
                            .toUri();
        
        return ResponseEntity.created( uri )
                             .body( memoryGameMapper.convertEntityToDetailsDto( memoryGame ) );
    }
    
    @PutMapping( "/{memoryGame}" )
    public ResponseEntity updateMemoryGame( @PathVariable( value = "memoryGame" ) String memoryGameName,
                                            @RequestBody MemoryGameUpdateDto memoryGameUpdateDto ) throws Exception {
        System.out.println("Olá!");
        var memoryGame = memoryGameService.updateMemoryGame( memoryGameName, memoryGameUpdateDto );
        
        return ResponseEntity.ok( memoryGameMapper.convertEntityToDetailsDto( memoryGame ));
    }
}