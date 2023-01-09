package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;

import java.util.Optional;

@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGameEntity, Long>{
    Optional<MemoryGameEntity> findByUserAndMemoryGame( UserEntity user, String memoryGame );
}