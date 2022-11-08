package com.tcc.jogodememoria.backend.user.interfaces;

import com.tcc.jogodememoria.backend.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);
}
