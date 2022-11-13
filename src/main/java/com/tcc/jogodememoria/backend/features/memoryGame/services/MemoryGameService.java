package com.tcc.jogodememoria.backend.features.memoryGame.services;

import com.tcc.jogodememoria.backend.features.memoryGame.interfaces.IMemoryGameRepository;
import com.tcc.jogodememoria.backend.features.memoryGame.interfaces.IMemoryGameService;
import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryGameService implements IMemoryGameService {

    MemoryGameService(IMemoryGameRepository memoryGameRepo) {
        this.memoryGameRepo = memoryGameRepo;
    }

    final IMemoryGameRepository memoryGameRepo;

    @Override
    public boolean existsByUser(UserModel user) {
        return memoryGameRepo.existsByUser(user);
    }

    @Override
    public List<MemoryGameModel> findByUser(UserModel user) {
        return memoryGameRepo.findByUser(user);
    }

    @Override
    public boolean existsByUserAndName(UserModel user, String name) {
        return memoryGameRepo.existsByUserAndName(user, name);
    }

    @Override
    public MemoryGameModel save(MemoryGameModel memoryGame) {
        return memoryGameRepo.save(memoryGame);
    }

    @Override
    public void delete(MemoryGameModel memoryGame) {
        memoryGameRepo.delete(memoryGame);
    }
}
