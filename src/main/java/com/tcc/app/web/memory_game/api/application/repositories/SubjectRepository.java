package com.tcc.app.web.memory_game.api.application.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long>{
    Optional<SubjectEntity> findByName(String name);
}
