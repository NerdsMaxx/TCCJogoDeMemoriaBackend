package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity,Long> {
    Optional<SubjectEntity> findBySubject(String subject);
    
    @Query("SELECT DISTINCT sub " +
           "FROM PlayerEntity pl " +
           "JOIN pl.memoryGameSet mg " +
           "JOIN mg.subjectSet sub " +
           "JOIN pl.user us " +
           "WHERE us.username = :username")
    Set<SubjectEntity> findAllByUsernamePlayer(String username);
    
    @Query("SELECT DISTINCT sub " +
           "FROM PlayerEntity pl " +
           "JOIN pl.memoryGameSet mg " +
           "JOIN mg.subjectSet sub " +
           "JOIN pl.user us " +
           "WHERE us.username = :username " +
           "AND mg.creator = :creator")
    Set<SubjectEntity> findAllByUsernamePlayerAndCreator(String username, CreatorEntity creator);
    
    @Query("SELECT DISTINCT sub " +
           "FROM MemoryGameEntity mg " +
           "JOIN mg.subjectSet sub " +
           "WHERE mg = :memoryGame")
    Set<SubjectEntity> findByMemoryGame(MemoryGameEntity memoryGame);
    
    @Query("SELECT sub " +
           "FROM SubjectEntity sub " +
           "WHERE sub.subject in  :subjectSet")
    Set<SubjectEntity> findBySubjectSet(Set<String> subjectSet);
    
    @Query("DELETE FROM SubjectEntity sub " +
           "WHERE sub.subject in :subjectSet")
    Set<SubjectEntity> deleteBySubjectSet(Set<String> subjectSet);
}