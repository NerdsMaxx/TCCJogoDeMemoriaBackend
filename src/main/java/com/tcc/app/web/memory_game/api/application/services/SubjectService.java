package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.AuthenticatedUserUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private AuthenticatedUserUtil authenticatedUserUtil;
    
    public Optional<SubjectEntity> findBySubjectName(String subjectName) {
        return subjectRepository.findBySubject(subjectName);
    }
    
    public List<SubjectEntity> findAllByPlayer(String username) throws Exception {
        Optional<CreatorEntity> optionalCreator = authenticatedUserUtil.getCurrentOptionalCreator();
        if (optionalCreator.isPresent()) {
            return _findAllByPlayerAndCreator(username, optionalCreator.get());
        }
        
        return subjectRepository.findAllByUsernamePlayer(username);
    }
    
    public List<SubjectEntity> save(List<String> subjectNameSet, MemoryGameEntity memoryGame) {
        List<SubjectEntity> subjectList = new LinkedList<>();
        
        for (String subjectName : subjectNameSet) {
            SubjectEntity subject = subjectRepository.findBySubject(subjectName)
                                                     .orElseGet(() -> new SubjectEntity(subjectName));
            
            subject.addMemoryGame(memoryGame);
            
            subjectRepository.save(subject);
            
            subjectList.add(subject);
        }
        
        return subjectList;
    }
    
    public List<SubjectEntity> update(List<String> subjectNameList, MemoryGameEntity memoryGame) throws Exception {
        _removeMemoryGameIfNotUsed(subjectNameList, memoryGame);
        
        List<SubjectEntity> subjectList = new LinkedList<>();
        
        for (String subjectName : subjectNameList) {
            SubjectEntity subject = subjectRepository.findBySubject(subjectName)
                                                     .orElse(new SubjectEntity(subjectName));
            
            subject.addMemoryGame(memoryGame);
            subjectRepository.save(subject);
            
            subjectList.add(subject);
        }
        
        return subjectList;
    }
    
    public void deleteByMemoryGameAndUser(MemoryGameEntity memoryGame) {
        List<String> empty = new LinkedList<>();
        _removeMemoryGameIfNotUsed(empty, memoryGame);
    }
    
    private void _removeMemoryGameIfNotUsed(List<String> subjectNameList, MemoryGameEntity memoryGame) {
        for (SubjectEntity subject : memoryGame.getSubjectList()) {
            if (! subjectNameList.contains(subject.getSubject())) {
                subject.removeMemoryGame(memoryGame);
                
                if (subject.getMemoryGameList().isEmpty()) {subjectRepository.delete(subject);} else {
                    subjectRepository.save(subject);
                }
            }
        }
    }
    
    private List<SubjectEntity> _findAllByPlayerAndCreator(String username, CreatorEntity creator) throws Exception {
        if (! playerService.existsByUsernameAndCreator(username, creator)) {
            throw new EntityNotFoundException("Este usuário não foi adicionado pelo professor.");
        }
        
        return subjectRepository.findAllByUsernamePlayerAndCreator(username, creator);
    }
}