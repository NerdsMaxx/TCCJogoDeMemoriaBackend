package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
import com.tcc.app.web.memory_game.api.application.repositories.PlayerRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;
    
    public PlayerEntity save(PlayerEntity player) {
        return playerRepository.save(player);
    }
    
    public PlayerEntity saveByUser(UserEntity user) {
        return playerRepository.save(new PlayerEntity(user));
    }
    
    public PlayerEntity findByUser(UserEntity user) throws Exception {
        return playerRepository.findByUser(user)
                               .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado!"));
    }
    
    public PlayerEntity findByUsername(String username) throws Exception {
        return playerRepository.findByUsername(username)
                               .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado!"));
    }
    
    boolean existsByUsernameAndCreator(String username, CreatorEntity creator) {
        return playerRepository.existsByUsernameAndCreator(username, creator);
    }
}