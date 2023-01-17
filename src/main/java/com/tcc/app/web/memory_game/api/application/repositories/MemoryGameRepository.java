package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGameEntity, Long> {
    Optional<MemoryGameEntity> findByUserAndMemoryGame(UserEntity user, String memoryGame);
    
    Page<MemoryGameEntity> findAllByUser(Pageable pageable, UserEntity user);
    
    boolean existsByUserAndMemoryGame(UserEntity user, String memoryGame);
}