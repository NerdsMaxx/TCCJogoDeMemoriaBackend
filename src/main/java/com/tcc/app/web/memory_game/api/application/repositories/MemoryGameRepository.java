package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGameEntity,Long> {
    Optional<MemoryGameEntity> findByCreatorAndMemoryGame(UserEntity creator, String memoryGame);
    
    @Query(
            "SELECT mg " +
            "FROM MemoryGameEntity mg " +
            "JOIN mg.creator cr " +
            "WHERE cr.username = :creatorUsername " +
            "AND mg.memoryGame = :memoryGame "
    )
    Optional<MemoryGameEntity> findByCreatorUsernameAndMemoryGame(String creatorUsername, String memoryGame);
    
    Set<MemoryGameEntity> findAllByCreator(UserEntity creator);
    
    boolean existsByCreatorAndMemoryGame(UserEntity creator, String memoryGame);
    
    @Query("SELECT DISTINCT mg " +
           "FROM PlayerGameplayEntity plg " +
           "JOIN plg.gameplay gm " +
           "JOIN gm.memoryGame mg " +
           "WHERE plg.player = :player")
    Set<MemoryGameEntity> findAllByPlayer(UserEntity player);
    
    @Query("SELECT DISTINCT mg " +
           "FROM MemoryGameEntity mg " +
           "JOIN mg.creator cr " +
           "JOIN mg.subjectSet sub " +
           "WHERE cr = :creator " +
           "AND (sub.subject LIKE :subject% " +
           "OR mg.memoryGame LIKE :memoryGameName%) ")
    Set<MemoryGameEntity> findAllBySubjectOrMemoryGameNameForCreator(UserEntity creator, String subject, String memoryGameName);
    
    @Query("SELECT DISTINCT mg " +
           "FROM PlayerGameplayEntity plg " +
           "JOIN plg.gameplay gm " +
           "JOIN gm.memoryGame mg " +
           "JOIN mg.subjectSet sub " +
           "WHERE plg.player = :player " +
           "AND (sub.subject LIKE :subject% " +
           "OR mg.memoryGame LIKE :memoryGameName%) ")
    Set<MemoryGameEntity> findAllBySubjectOrMemoryGameNameForPlayer(UserEntity player, String subject, String memoryGameName);
}