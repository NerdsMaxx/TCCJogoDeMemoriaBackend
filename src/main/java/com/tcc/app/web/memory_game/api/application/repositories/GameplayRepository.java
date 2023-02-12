package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameplayRepository extends JpaRepository<GameplayEntity, Long> {

}