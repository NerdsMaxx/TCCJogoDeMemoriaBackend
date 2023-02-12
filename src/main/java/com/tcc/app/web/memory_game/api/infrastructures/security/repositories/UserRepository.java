package com.tcc.app.web.memory_game.api.infrastructures.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	
	@Query("SELECT us " +
		   "FROM UserEntity us " +
		   "WHERE us.username = :usernameOrEmail " +
		   "OR us.email = :usernameOrEmail")
	UserEntity findByUsernameOrEmail(String usernameOrEmail);
}