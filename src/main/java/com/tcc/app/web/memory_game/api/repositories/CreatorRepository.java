//package com.tcc.app.web.memory_game.api.application.repositories;
//
//import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
//import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface CreatorRepository extends JpaRepository<CreatorEntity,Long> {
//
//    Optional<CreatorEntity> findByUser(UserEntity user);
//
//    @Query("SELECT cr " +
//           "FROM UserEntity us " +
//           "JOIN us.creator cr " +
//           "WHERE us.username = :username")
//    Optional<CreatorEntity> findByUsername(String username);
//}