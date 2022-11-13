package com.tcc.jogodememoria.backend.features.user.interfaces;

import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByUsername(String username);

    Optional<UserModel> findByUsername(String username);
}
