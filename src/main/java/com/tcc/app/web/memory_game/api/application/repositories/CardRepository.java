package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CardEntity;
import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
    
    @Query("SELECT c " +
           "FROM CardEntity c " +
           "JOIN c.memoryGame mg " +
           "JOIN mg.creator cr " +
           "WHERE cr = :creator " +
           "AND mg = :memoryGame ")
    Set<CardEntity> findAllByMemoryGameAndCreator(MemoryGameEntity memoryGame, CreatorEntity creator);
}