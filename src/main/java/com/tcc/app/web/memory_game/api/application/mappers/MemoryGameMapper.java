package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.responses.MemoryGameDetailsDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.interfaces.MapperEntityToDetailsDtoInterface;
import com.tcc.app.web.memory_game.api.infrastructures.security.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MemoryGameMapper
        implements MapperEntityToDetailsDtoInterface<MemoryGameEntity, MemoryGameDetailsDto> {
    
    @Autowired
    private CardMapper cardMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public MemoryGameDetailsDto convertEntityToDetailsDto( MemoryGameEntity memoryGame ) {
        var userDetailsDto = userMapper.convertEntityToDetailsDto( memoryGame.getUser() );
        
        var name = memoryGame.getMemoryGame();
        
        var subjectSet = memoryGame.getSubjectSet().stream()
                                   .map( SubjectEntity::getSubject )
                                   .collect( Collectors.toSet() );
        
        var cardSet = memoryGame.getCardSet().stream()
                                .map( card -> cardMapper.convertEntityToDetailsDto( card ) )
                                .collect( Collectors.toSet() );
        
        return new MemoryGameDetailsDto( userDetailsDto, name, subjectSet, cardSet );
    }
}