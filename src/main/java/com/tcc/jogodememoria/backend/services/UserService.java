package com.tcc.jogodememoria.backend.services;

import com.tcc.jogodememoria.backend.interfaces.repositories.IUserRepository;
import com.tcc.jogodememoria.backend.interfaces.services.IUserService;
import com.tcc.jogodememoria.backend.models.UserModel;
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
    
    @Override
    public boolean existsByUsernameAndPassword (String username, String password) {
        return userRepo.existsByUsernameAndPassword(username, password);
    }
    
    public UserModel save (UserModel user) {
        return userRepo.save(user);
    }
    
    public void delete (UserModel user) {
        userRepo.delete(user);
    }
}