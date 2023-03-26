package com.tcc.app.web.memory_game.api.infrastructures.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	
	@Query("SELECT us " +
		   "FROM UserEntity us " +
		   "WHERE us.username = :usernameOrEmail " +
		   "OR us.email = :usernameOrEmail")
	UserEntity findByUsernameOrEmail(String usernameOrEmail);
	
	
	@Query("SELECT us " +
		   "FROM UserEntity us " +
		   "JOIN us.userType ut " +
		   "WHERE (us.username = :usernameOrEmail " +
		   "OR us.email = :usernameOrEmail) " +
		   "AND ut.type = 0" )
	Optional<UserEntity> findCreatorByUsernameOrEmail(String usernameOrEmail);
	
	@Query("SELECT us " +
		   "FROM UserEntity us " +
		   "JOIN us.userType ut " +
		   "WHERE (us.username = :usernameOrEmail " +
		   "OR us.email = :usernameOrEmail) " +
		   "AND ut.type = 1" )
	Optional<UserEntity> findPlayerByUsernameOrEmail(String usernameOrEmail);
}