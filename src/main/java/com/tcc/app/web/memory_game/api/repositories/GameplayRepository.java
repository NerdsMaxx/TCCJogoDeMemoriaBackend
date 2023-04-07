package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameplayRepository extends JpaRepository<GameplayEntity, Long> {

}