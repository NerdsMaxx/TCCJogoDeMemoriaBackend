package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGameEntity, Long> {
    
    @Query("SELECT mg " +
           "FROM MemoryGameEntity mg " +
           "JOIN mg.creator cr " +
           "WHERE cr = :creator " +
           "AND mg.memoryGame = :memoryGame")
    Optional<MemoryGameEntity> findByCreatorAndMemoryGame(CreatorEntity creator, String memoryGame);
    
    Page<MemoryGameEntity> findAllByCreator(Pageable pageable, CreatorEntity creator);
    
    boolean existsByCreatorAndMemoryGame(CreatorEntity creator, String memoryGame);
}