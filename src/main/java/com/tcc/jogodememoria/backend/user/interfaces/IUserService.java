package com.tcc.jogodememoria.backend.user.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tcc.jogodememoria.backend.user.models.UserModel;

public interface IUserService {

    public UserModel save(UserModel userModel);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public List<UserModel> findAll();

    public Optional<UserModel> findById(UUID id);

    public Optional<UserModel> findByUsername(String username);

    public Optional<UserModel> findByEmail(String email);

    public void delete(UserModel userModel);
}
