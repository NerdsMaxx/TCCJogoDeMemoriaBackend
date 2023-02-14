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
import java.util.Set;

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
    
    public Set<SubjectEntity> findAllByPlayer(String username) throws Exception {
        Optional<CreatorEntity> optionalCreator = authenticatedUserUtil.getCurrentOptionalCreator();
        if (optionalCreator.isPresent()) {
            return _findAllByPlayerAndCreator(username, optionalCreator.get());
        }
        
        return subjectRepository.findAllByUsernamePlayer(username);
    }
    
    private Set<SubjectEntity> _findAllByPlayerAndCreator(String username, CreatorEntity creator) throws Exception {
        if (! playerService.existsByUsernameAndCreator(username, creator)) {
            throw new EntityNotFoundException("Este usuário não foi adicionado pelo professor.");
        }
        
        return subjectRepository.findAllByUsernamePlayerAndCreator(username, creator);
    }
}