package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerGameplayRepository extends JpaRepository<PlayerGameplayEntity, Long> {
    
    Optional<PlayerGameplayEntity> findByPlayerAndGameplay(PlayerEntity player, GameplayEntity gameplay);
    
    boolean existsByPlayerAndGameplay(PlayerEntity player, GameplayEntity gameplay);
}