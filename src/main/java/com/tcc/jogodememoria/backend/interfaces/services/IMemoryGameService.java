package com.tcc.jogodememoria.backend.interfaces.services;

import com.tcc.jogodememoria.backend.interfaces.IService;
import com.tcc.jogodememoria.backend.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.models.UserModel;

import java.util.List;
import java.util.Optional;

public interface IMemoryGameService extends IService<MemoryGameModel> {
    
    List<MemoryGameModel> findAll ();
    
    boolean existsByUser (UserModel user);
    
    List<MemoryGameModel> findByUser (UserModel user);
    
    boolean existsByUserAndName (UserModel user, String name);
    
    Optional<MemoryGameModel> findByUserAndName (UserModel user, String name);
}