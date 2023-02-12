package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.repositories.CreatorRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CreatorService {
    
    @Autowired
    private CreatorRepository creatorRepository;
    
    public CreatorEntity saveCreator(UserEntity user) {
        return creatorRepository.save(new CreatorEntity(user));
    }
    
    public CreatorEntity findByUser(UserEntity user) throws Exception {
        return creatorRepository.findByUser(user)
                                .orElseThrow(() -> new EntityNotFoundException("Criador não encontrado!"));
    }
    
    public Optional<CreatorEntity> findOptionalByUser(UserEntity user) {
        return creatorRepository.findByUser(user);
    }
    
    public CreatorEntity findByUsername(String username) throws Exception {
        return creatorRepository.findByUsername(username)
                                .orElseThrow(() -> new EntityNotFoundException("Criador não encontrado!"));
    }
}