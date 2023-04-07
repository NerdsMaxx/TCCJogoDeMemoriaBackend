package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
		   "AND ut.type = 'CRIADOR'" )
	Optional<UserEntity> findCreatorByUsernameOrEmail(String usernameOrEmail);
	
	@Query("SELECT us " +
		   "FROM UserEntity us " +
		   "JOIN us.userType ut " +
		   "WHERE (us.username = :usernameOrEmail " +
		   "OR us.email = :usernameOrEmail) " +
		   "AND ut.type = 'JOGADOR'" )
	Optional<UserEntity> findPlayerByUsernameOrEmail(String usernameOrEmail);
}