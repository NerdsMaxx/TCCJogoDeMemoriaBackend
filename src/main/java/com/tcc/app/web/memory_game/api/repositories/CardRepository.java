package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.CardEntity;
import com.tcc.app.web.memory_game.api.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<CardEntity,Long> {
    
    @Query("SELECT c " +
           "FROM CardEntity c " +
           "JOIN c.memoryGame mg " +
           "JOIN mg.creator cr " +
           "WHERE cr = :creator " +
           "AND mg = :memoryGame ")
    Set<CardEntity> findAllByMemoryGameAndCreator(MemoryGameEntity memoryGame, UserEntity creator);
}