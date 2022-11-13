package com.tcc.jogodememoria.backend.features.memoryGame.interfaces;

import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IMemoryGameRepository extends JpaRepository<MemoryGameModel, UUID> {
    boolean existsByUser(UserModel user);

    List<MemoryGameModel> findByUser(UserModel user);
}
