package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlayerGameplayRepository extends JpaRepository<PlayerGameplayEntity,Long> {
    
    Optional<PlayerGameplayEntity> findByPlayerAndGameplay(UserEntity player, GameplayEntity gameplay);
    
    boolean existsByPlayerAndGameplay(UserEntity player, GameplayEntity gameplay);
    
    @Query("SELECT plgm " +
           "FROM PlayerGameplayEntity plgm " +
           "JOIN plgm.gameplay gm " +
           "WHERE gm.id = :gameplayId " +
           "AND plgm.endTime IS NOT NULL " +
           "ORDER BY plgm.startTime DESC")
    List<PlayerGameplayEntity> findAllWithScoresByGameplayId(Long gameplayId);
    
    @Query("SELECT plgm " +
           "FROM PlayerGameplayEntity plgm " +
           "JOIN plgm.gameplay gm " +
           "WHERE gm.id = :gameplayId " +
           "AND plgm.player = :player " +
           "AND gm.alone = true " +
           "AND plgm.endTime IS NOT NULL " +
           "ORDER BY plgm.startTime DESC")
    Optional<PlayerGameplayEntity> findByPlayerAndGameplayId(UserEntity player, Long gameplayId);
    
    @Query("SELECT plgm " +
           "FROM PlayerGameplayEntity plgm " +
           "JOIN plgm.player pl " +
           "WHERE pl = :player " +
           "AND plgm.endTime IS NOT NULL " +
           "ORDER BY plgm.startTime DESC")
   List<PlayerGameplayEntity> findAllByPlayer(UserEntity player);
}