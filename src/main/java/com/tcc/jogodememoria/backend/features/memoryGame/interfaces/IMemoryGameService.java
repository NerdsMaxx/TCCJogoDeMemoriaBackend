package com.tcc.jogodememoria.backend.features.memoryGame.interfaces;

import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import com.tcc.jogodememoria.backend.interfaces.IService;

import java.util.List;

public interface IMemoryGameService extends IService<MemoryGameModel> {
    boolean existsByUser(UserModel user);

    List<MemoryGameModel> findByUser(UserModel user);
}
