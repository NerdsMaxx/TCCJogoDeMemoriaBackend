package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameplayRepository extends JpaRepository<GameplayEntity, Long> {
    
    @Query("SELECT gply " +
           "FROM GameplayEntity gply " +
           "JOIN gply.memoryGame mg " +
           "WHERE mg.creator = :creator " +
           "AND gply.alone = false")
    List<GameplayEntity> findPreviousGameplaysByCreator(UserEntity creator);
}