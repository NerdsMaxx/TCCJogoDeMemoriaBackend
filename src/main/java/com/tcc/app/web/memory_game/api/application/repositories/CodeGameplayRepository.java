package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeGameplayRepository extends JpaRepository<CodeGameplayEntity, Long> {
    
    Optional<CodeGameplayEntity> findByCode(String code);
    
    @Query("SELECT gply " +
           "FROM CodeGameplayEntity cg " +
           "JOIN cg.gameplay gply " +
           "WHERE cg.code = :code")
    Optional<GameplayEntity> findGameplayByCode(String code);
    
    boolean existsByCode(String code);
}