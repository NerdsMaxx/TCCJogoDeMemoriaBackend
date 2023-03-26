package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PlayerGameplayRepository extends JpaRepository<PlayerGameplayEntity,Long> {
    
    Optional<PlayerGameplayEntity> findByPlayerAndGameplay(UserEntity player, GameplayEntity gameplay);
    
    boolean existsByPlayerAndGameplay(UserEntity player, GameplayEntity gameplay);

//    @Query("SELECT plgm " +
//           "FROM PlayerGameplayEntity plgm " +
//           "JOIN plgm.gameplay gm " +
//           "WHERE gm = :gameplay " +
//           "AND (plgm.numberPairCardCorrect > 0 " +
//           "OR plgm.numberPairCardWrong > 0)")
//    Set<PlayerGameplayEntity> findAllWithScoresByGameplay(GameplayEntity gameplay);
    
    @Query("SELECT plgm " +
           "FROM PlayerGameplayEntity plgm " +
           "JOIN plgm.gameplay gm " +
           "WHERE gm = :gameplay")
    Set<PlayerGameplayEntity> findAllWithScoresByGameplay(GameplayEntity gameplay);
    
    @Query("SELECT plgm " +
           "FROM PlayerGameplayEntity plgm " +
           "JOIN plgm.player pl " +
           "WHERE pl = :player")
    Set<PlayerGameplayEntity> findAllByPlayer(UserEntity player);
}