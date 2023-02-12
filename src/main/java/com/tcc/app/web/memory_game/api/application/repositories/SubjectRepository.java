package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity,Long> {
    Optional<SubjectEntity> findBySubject(String subject);
    
    @Query("SELECT DISTINCT sub " +
           "FROM PlayerEntity pl " +
           "JOIN pl.memoryGameList mg " +
           "JOIN mg.subjectList sub " +
           "JOIN pl.user us " +
           "WHERE us.username = :username")
    List<SubjectEntity> findAllByUsernamePlayer(String username);
    
    @Query("SELECT DISTINCT sub " +
           "FROM PlayerEntity pl " +
           "JOIN pl.memoryGameList mg " +
           "JOIN mg.subjectList sub " +
           "JOIN pl.user us " +
           "WHERE us.username = :username " +
           "AND mg.creator = :creator")
    List<SubjectEntity> findAllByUsernamePlayerAndCreator(String username, CreatorEntity creator);
    
    @Query("SELECT DISTINCT sub " +
           "FROM MemoryGameEntity mg " +
           "JOIN mg.subjectList sub " +
           "WHERE mg = :memoryGame")
    List<SubjectEntity> findByMemoryGame(MemoryGameEntity memoryGame);
}