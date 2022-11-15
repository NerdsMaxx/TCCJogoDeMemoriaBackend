package com.tcc.jogodememoria.backend.features.user.interfaces;

import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import com.tcc.jogodememoria.backend.interfaces.IService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IService<UserModel> {
    
    List<UserModel> findAll ();
    
    boolean existsByUsername (String username);
    
    Optional<UserModel> findByUsername (String username);
    
    boolean existsByUsernameAndEmail (String username, String email);
    
    boolean existsByEmail (String email);
}
