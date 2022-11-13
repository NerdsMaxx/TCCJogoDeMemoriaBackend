package com.tcc.jogodememoria.backend.features.user.interfaces;

import com.tcc.jogodememoria.backend.features.user.models.UserModel;

import java.util.Optional;

public interface IUserService {
    boolean existsByUsername(String username);

    Optional<UserModel> findByUsername(String username);

    UserModel save(UserModel user);

    void delete(UserModel user);
}
