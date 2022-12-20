package com.tcc.app.web.memory_game.api.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;

@Repository
public interface MemoryGameRepository extends JpaRepository<MemoryGameEntity, Long>{

}
