package com.tcc.jogodememoria.backend.user.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcc.jogodememoria.backend.user.interfaces.IUserRepository;
import com.tcc.jogodememoria.backend.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.user.models.UserModel;

@Service
public class UserService implements IUserService {
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    final IUserRepository userRepository;

    @Override
    @Transactional
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }
}
