package com.tcc.jogodememoria.backend.features.user.services;

import com.tcc.jogodememoria.backend.features.user.interfaces.IUserRepository;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements com.tcc.jogodememoria.backend.features.user.interfaces.IUserService {

    public final IUserRepository userRepo;

    UserService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public UserModel save(UserModel user) {
        return userRepo.save(user);
    }

    public void delete(UserModel user) {
        userRepo.delete(user);
    }
}
