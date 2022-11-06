package com.tcc.jogodememoria.backend.user.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.jogodememoria.backend.user.models.UserModel;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);
}
