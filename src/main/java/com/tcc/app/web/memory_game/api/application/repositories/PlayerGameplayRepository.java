package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PlayerGameplayRepository extends JpaRepository<PlayerGameplayEntity, Long> {
    
    Optional<PlayerGameplayEntity> findByPlayerAndGameplay(PlayerEntity player, GameplayEntity gameplay);
    
    boolean existsByPlayerAndGameplay(PlayerEntity player, GameplayEntity gameplay);
    
    @Query("SELECT plgm " +
           "FROM PlayerGameplayEntity plgm " +
           "JOIN plgm.gameplay gm " +
           "WHERE gm = :gameplay " +
           "AND (plgm.numberPairCardCorrect > 0 " +
           "OR plgm.numberPairCardWrong > 0)")
    Set<PlayerGameplayEntity> findAllWithScoresByGameplay(GameplayEntity gameplay);
}