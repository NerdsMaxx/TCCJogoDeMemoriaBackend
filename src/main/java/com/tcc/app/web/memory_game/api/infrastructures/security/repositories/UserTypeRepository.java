package com.tcc.app.web.memory_game.api.infrastructures.security.repositories;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeEntity, Integer> {
    Optional<UserTypeEntity> findByType( String type );
}