package com.tcc.jogodememoria.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tcc.jogodememoria.backend.interfaces.repositories.IMemoryGameRepository;
import com.tcc.jogodememoria.backend.interfaces.services.IMemoryGameService;
import com.tcc.jogodememoria.backend.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.models.UserModel;

@Service
public class MemoryGameService implements IMemoryGameService {
    
    MemoryGameService (final IMemoryGameRepository memoryGameRepo) {
        this.memoryGameRepo = memoryGameRepo;
    }
    
    final IMemoryGameRepository memoryGameRepo;
    
    @Override
    public List<MemoryGameModel> findAll () {
        return memoryGameRepo.findAll();
    }
    
    @Override
    public boolean existsByUser (final UserModel user) {
        return memoryGameRepo.existsByUser(user);
    }
    
    @Override
    public List<MemoryGameModel> findByUser (final UserModel user) {
        return memoryGameRepo.findByUser(user);
    }
    
    @Override
    public boolean existsByUserAndName (final UserModel user, final String name) {
        return memoryGameRepo.existsByUserAndName(user,
                                                  name);
    }
    
    @Override
    public Optional<MemoryGameModel> findByUserAndName (final UserModel user, final String name) {
        return memoryGameRepo.findByUserAndName(user,
                                                name);
    }
    
    @Override
    public MemoryGameModel save (final MemoryGameModel memoryGame) {
        return memoryGameRepo.save(memoryGame);
    }
    
    @Override
    public void delete (final MemoryGameModel memoryGame) {
        memoryGameRepo.delete(memoryGame);
    }
    
}