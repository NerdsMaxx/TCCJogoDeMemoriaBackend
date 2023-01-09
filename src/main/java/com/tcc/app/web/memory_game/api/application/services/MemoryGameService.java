package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.dtos.requests.insert.MemoryGameInsertDto;
import com.tcc.app.web.memory_game.api.application.dtos.requests.update.MemoryGameUpdateDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.repositories.MemoryGameRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemoryGameService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private MemoryGameRepository memoryGameRepository;
    
    @Transactional
    public MemoryGameEntity registerNewMemoryGame( MemoryGameInsertDto memoryGameInsertDto ) {
        var user = userService.getCurrentUser();
        
        var memoryGame = new MemoryGameEntity();
        memoryGame.setMemoryGame( memoryGameInsertDto.name() );
        memoryGame.setUser( user );
        
        memoryGameRepository.save( memoryGame );
        
        var cardSet = cardService.registerNewCardsForMemoryGame( memoryGameInsertDto.cardSet(),
                                                                 memoryGame, user );
        var subjectSet = subjectService.registerNewSubjectsForMemoryGame(
                memoryGameInsertDto.subjectSet(), memoryGame, user );
        
        memoryGame.setCardSet( cardSet );
        memoryGame.setSubjectSet( subjectSet );
        
        userService.addSubjectSetForUser( user, subjectSet );
        
        return memoryGame;
    }
    
    @Transactional
    public MemoryGameEntity updateMemoryGame( String memoryGameName, MemoryGameUpdateDto memoryGameUpdateDto ) throws Exception {
        var user = userService.getCurrentUser();
        
        var optionalMemoryGame = memoryGameRepository.findByUserAndMemoryGame( user,
                                                                               memoryGameName );
        if ( optionalMemoryGame.isEmpty() ) {
            throw new Exception( "Não foi encontrado jogo de memória." );
        }
        System.out.println("Aqui passa?");
        
        var memoryGame = optionalMemoryGame.get();
        
        if ( memoryGameUpdateDto.name() != null ) {
            memoryGame.setMemoryGame( memoryGameUpdateDto.name() );
            System.out.println("Aqui nome foi alterado!!");
        }
        
        if ( memoryGameUpdateDto.cardSet() != null ) {
            memoryGame.getCardSet().clear();
            
            var cardSet = cardService.updateCardsForMemoryGame(
                    memoryGameUpdateDto.cardSet(), memoryGame, user );
            
            memoryGame.setCardSet( cardSet );
            System.out.println("Aqui as cartas foram alterados!!");
        }
        
        if ( memoryGameUpdateDto.subjectSet() != null ) {
            memoryGame.getSubjectSet().clear();
            
            var subjectSet = subjectService.updateSubjectsForMemoryGame(
                    memoryGameUpdateDto.subjectSet(), memoryGame, user );
            
            memoryGame.setSubjectSet( subjectSet );
            userService.addSubjectSetForUser( user, subjectSet );
            
            System.out.println("Aqui as matérias foram alteradas!!");
        }
        
        memoryGameRepository.save( memoryGame );
        System.out.println("Foi atualizado o jogo de memoria");
        
        return memoryGame;
    }
    
}