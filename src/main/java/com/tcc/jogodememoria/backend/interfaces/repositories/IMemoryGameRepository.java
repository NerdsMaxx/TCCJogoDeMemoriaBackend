package com.tcc.jogodememoria.backend.interfaces.repositories;

import com.tcc.jogodememoria.backend.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IMemoryGameRepository extends JpaRepository<MemoryGameModel, UUID> {
    boolean existsByUser (UserModel user);
    
    List<MemoryGameModel> findByUser (UserModel user);
    
    boolean existsByUserAndName (UserModel user, String name);
    
    Optional<MemoryGameModel> findByUserAndName (UserModel user, String name);
}