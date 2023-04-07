//package com.tcc.app.web.memory_game.api.application.repositories;
//
//import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
//import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
//import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface PlayerRepository extends JpaRepository<PlayerEntity,Long> {
//
//    Optional<PlayerEntity> findByUser(UserEntity user);
//
//    @Query("SELECT pl " +
//           "FROM PlayerEntity pl " +
//           "JOIN pl.user us " +
//           "WHERE us.username = :username")
//    Optional<PlayerEntity> findByUsername(String username);
//
//
//    @Query("SELECT pl " +
//           "FROM CreatorEntity cr " +
//           "JOIN cr.memoryGameSet mg " +
//           "JOIN mg.playerSet pl " +
//           "JOIN pl.user us " +
//           "WHERE cr = :creator " +
//           "AND us.username = :username")
//     Optional<PlayerEntity> findByUsernameAndCreator(String username, CreatorEntity creator);
//
//
//    @Query("SELECT CASE WHEN COUNT(pl) > 0 " +
//           "THEN TRUE ELSE FALSE END " +
//           "FROM CreatorEntity cr " +
//           "JOIN cr.memoryGameSet mg " +
//           "JOIN mg.playerSet pl " +
//           "JOIN pl.user us " +
//           "WHERE cr = :creator " +
//           "AND us.username = :username")
//    boolean existsByUsernameAndCreator(String username, CreatorEntity creator);
//}