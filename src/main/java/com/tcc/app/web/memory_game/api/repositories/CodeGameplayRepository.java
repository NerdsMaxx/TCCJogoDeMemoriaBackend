package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CodeGameplayRepository extends JpaRepository<CodeGameplayEntity,Long> {
    
    Optional<CodeGameplayEntity> findByCode(String code);
    
    @Query("SELECT gply " +
           "FROM CodeGameplayEntity cg " +
           "JOIN cg.gameplay gply " +
           "WHERE cg.code = :code")
    Optional<GameplayEntity> findGameplayByCode(String code);
    
    boolean existsByCode(String code);
    
    
    @Query("SELECT code " +
           "FROM CodeGameplayEntity code " +
           "JOIN code.gameplay gply " +
           "JOIN gply.memoryGame mg " +
           "JOIN mg.creator cr " +
           "WHERE cr = :creator " +
           "AND gply.alone = false")
    Set<CodeGameplayEntity> findCodeSetByCreator(UserEntity creator);
    
    @Modifying
    @Query("DELETE FROM " +
           "CodeGameplayEntity cg " +
           "WHERE CURRENT_TIMESTAMP >= cg.endTime")
    void deleteAllInvalidated();
}