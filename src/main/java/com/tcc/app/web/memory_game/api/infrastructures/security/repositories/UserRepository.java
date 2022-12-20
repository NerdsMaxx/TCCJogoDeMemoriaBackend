package com.tcc.app.web.memory_game.api.infrastructures.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
		UserDetails findByUsername( String username );
}
