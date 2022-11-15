package com.tcc.jogodememoria.backend.features.user.services;

import com.tcc.jogodememoria.backend.features.user.interfaces.IUserRepository;
import com.tcc.jogodememoria.backend.features.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    
    public final IUserRepository userRepo;
    
    UserService (IUserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    @Override
    public List<UserModel> findAll () {
        return userRepo.findAll();
    }
    
    public boolean existsByUsername (String username) {
        return userRepo.existsByUsername(username);
    }
    
    public Optional<UserModel> findByUsername (String username) {
        return userRepo.findByUsername(username);
    }
    
    @Override
    public boolean existsByUsernameAndEmail (String username, String email) {
        return userRepo.existsByUsernameAndEmail(username, email);
    }
    
    @Override
    public boolean existsByEmail (String email) {
        return userRepo.existsByEmail(email);
    }
    
    public UserModel save (UserModel user) {
        return userRepo.save(user);
    }
    
    public void delete (UserModel user) {
        userRepo.delete(user);
    }
}
