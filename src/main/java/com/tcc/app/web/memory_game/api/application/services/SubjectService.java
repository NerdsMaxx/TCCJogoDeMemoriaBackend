package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private UserService userService;
    
    
    Optional<SubjectEntity> findBySubjectName(String subjectName) {
        return subjectRepository.findBySubject(subjectName);
    }
    
    @Transactional
    public List<SubjectEntity> saveSubjects(List<String> subjectNameSet, MemoryGameEntity memoryGame, UserEntity user) {
        var subjectList = new LinkedList<SubjectEntity>();
        
        for (var subjectName : subjectNameSet) {
            var subject = subjectRepository.findBySubject(subjectName)
                                           .orElseGet(() -> new SubjectEntity(subjectName));
            
            subject.addMemoryGame(memoryGame);
            
            subjectRepository.save(subject);
            
            subjectList.add(subject);
        }
        
        return subjectList;
    }
    
    @Transactional
    public List<SubjectEntity> updateSubjects(List<String> subjectNameList, MemoryGameEntity memoryGame) throws Exception {
        removeMemoryGameIfNotUsed(subjectNameList, memoryGame);
        
        var subjectList = new LinkedList<SubjectEntity>();
        
        for (var subjectName : subjectNameList) {
            var subject = subjectRepository.findBySubject(subjectName).orElse(new SubjectEntity(subjectName));
            
            subject.addMemoryGame(memoryGame);
            subjectRepository.save(subject);
            
            subjectList.add(subject);
        }
        
        return subjectList;
    }
    
    @Transactional
    public void deleteSubjectsByMemoryGameAndUser(MemoryGameEntity memoryGame) {
        List<String> empty = new LinkedList<>();
        removeMemoryGameIfNotUsed(empty, memoryGame);
    }
    
    @Transactional
    private void removeMemoryGameIfNotUsed(List<String> subjectNameList, MemoryGameEntity memoryGame) {
        for (var subject : memoryGame.getSubjectList()) {
            if (! subjectNameList.contains(subject.getSubject())) {
                subject.removeMemoryGame(memoryGame);
                
                if (subject.getMemoryGameList().isEmpty()) {subjectRepository.delete(subject);}
                else {subjectRepository.save(subject);}
            }
        }
    }
}