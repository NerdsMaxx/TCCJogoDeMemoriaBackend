package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGameEntity,Long> {
    Optional<MemoryGameEntity> findByCreatorAndMemoryGame(CreatorEntity creator, String memoryGame);
    
    @Query(
            "SELECT mg " +
            "FROM MemoryGameEntity mg " +
            "JOIN mg.creator cr " +
            "JOIN cr.user us " +
            "WHERE us.username = :creatorUsername " +
            "AND mg.memoryGame = :memoryGame "
    )
    Optional<MemoryGameEntity> findByCreatorUsernameAndMemoryGame(String creatorUsername, String memoryGame);
    
    Page<MemoryGameEntity> findAllByCreator(Pageable pageable, CreatorEntity creator);
    
    boolean existsByCreatorAndMemoryGame(CreatorEntity creator, String memoryGame);
    
    @Query("SELECT DISTINCT mg " +
           "FROM PlayerGameplayEntity plg " +
           "JOIN plg.gameplay gm " +
           "JOIN gm.memoryGame mg " +
           "WHERE plg.player = :player")
    Page<MemoryGameEntity> findAllByPlayer(Pageable pageable, PlayerEntity player);
    
    @Query("SELECT DISTINCT mg " +
           "FROM MemoryGameEntity mg " +
           "JOIN mg.creator cr " +
           "JOIN mg.subjectSet sub " +
           "WHERE cr = :creator " +
           "AND (sub.subject LIKE :subject% " +
           "OR mg.memoryGame LIKE :memoryGameName%) ")
    Page<MemoryGameEntity> findAllBySubjectOrMemoryGameName(Pageable pageable, CreatorEntity creator, String subject, String memoryGameName);
    
    @Query("SELECT DISTINCT mg " +
           "FROM PlayerGameplayEntity plg " +
           "JOIN plg.gameplay gm " +
           "JOIN gm.memoryGame mg " +
           "JOIN mg.subjectSet sub " +
           "WHERE plg.player = :player " +
           "AND (sub.subject LIKE :subject% " +
           "OR mg.memoryGame LIKE :memoryGameName%) ")
    Page<MemoryGameEntity> findAllBySubjectOrMemoryGameName(Pageable pageable, PlayerEntity player, String subject, String memoryGameName);
}