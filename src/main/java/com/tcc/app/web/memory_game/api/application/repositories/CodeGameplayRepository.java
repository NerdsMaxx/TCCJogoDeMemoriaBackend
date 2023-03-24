package com.tcc.app.web.memory_game.api.application.repositories;

import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CodeGameplayRepository extends JpaRepository<CodeGameplayEntity, Long> {
    
    Optional<CodeGameplayEntity> findByCode(String code);
    
    @Query("SELECT gply " +
           "FROM CodeGameplayEntity cg " +
           "JOIN cg.gameplay gply " +
           "WHERE cg.code = :code")
    Optional<GameplayEntity> findGameplayByCode(String code);
    
    boolean existsByCode(String code);
    
    
    @Query("SELECT code " +
           "FROM CodeGameplayEntity code " +
           "JOIN FETCH code.gameplay gply " +
           "JOIN FETCH gply.memoryGame mg " +
           "JOIN FETCH mg.creator cr " +
           "JOIN FETCH cr.user us " +
           "WHERE cr = :creator " +
           "AND gply.alone = false")
    Set<CodeGameplayEntity> findCodeSetByCreator(CreatorEntity creator);
    
    @Modifying
    @Query("DELETE FROM " +
           "CodeGameplayEntity cg " +
           "WHERE CURRENT_TIMESTAMP >= cg.endTime")
    void deleteAllInvalidated();
}