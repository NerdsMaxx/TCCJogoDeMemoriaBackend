package com.tcc.jogodememoria.backend.user.interfaces;

import com.tcc.jogodememoria.backend.user.models.UserModel;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    public UserModel saveUserModel(UserModel userModel);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public List<UserModel> findAll();

    public Optional<UserModel> findById(Long id);

    public Optional<UserModel> findByUsername(String username);

    public Optional<UserModel> findByEmail(String email);

    public void deleteUserModel(UserModel userModel);
}
