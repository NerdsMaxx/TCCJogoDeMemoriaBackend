package com.tcc.app.web.memory_game.api.application.repositories;

import java.util.List;
import java.util.Optional;

import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long>{
    Optional<SubjectEntity> findBySubject(String subject);
}