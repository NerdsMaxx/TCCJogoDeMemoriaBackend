package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private UserService userService;
    
    @Transactional
    public Set<SubjectEntity> registerNewSubjectsForMemoryGame( Set<String> subjectNameSet,
                                                                MemoryGameEntity memoryGame, UserEntity user ) {
        var subjectSet = new HashSet<SubjectEntity>();
        
        for ( var subjectName : subjectNameSet ) {
            var optionalSubject = subjectRepository.findBySubject( subjectName );
            SubjectEntity subject;
            
            if ( optionalSubject.isPresent() ) {
                subject = optionalSubject.get();
            } else {
                subject = new SubjectEntity( subjectName );
                subject.setUserSet( new HashSet<>() );
                subject.setMemoryGameSet( new HashSet<>() );
            }
            
            subject.getUserSet().add( user );
            subject.getMemoryGameSet().add( memoryGame );
            
            if ( optionalSubject.isEmpty() ) {
                subject = subjectRepository.save( subject );
            }
            
            subjectSet.add( subject );
        }
        
        return subjectSet;
    }
    
    public Set<SubjectEntity> updateSubjectsForMemoryGame( Set<String> subjectNameSet, MemoryGameEntity memoryGame, UserEntity user ) {
        var subjectSet = new HashSet<SubjectEntity>();
        
        for ( var subjectName : subjectNameSet ) {
            var optionalSubject = subjectRepository.findBySubject( subjectName );
            SubjectEntity subject;
            
            if ( optionalSubject.isPresent() ) {
                subject = optionalSubject.get();
                subjectSet.add( subject );
                continue;
            }
            
            subject = new SubjectEntity( subjectName );
            subject.setUserSet( new HashSet<>() );
            subject.setMemoryGameSet( new HashSet<>() );
            
            subject.getUserSet().add( user );
            subject.getMemoryGameSet().add( memoryGame );
            
            subjectRepository.save( subject );
            
            subjectSet.add( subject );
        }
        
        return subjectSet;
    }
}