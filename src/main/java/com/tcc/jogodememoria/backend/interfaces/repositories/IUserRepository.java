package com.tcc.jogodememoria.backend.interfaces.repositories;

import com.tcc.jogodememoria.backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    
    boolean existsByUsername (String username);
    
    Optional<UserModel> findByUsername (String username);
    
    boolean existsByUsernameAndEmail (String username, String email);
    
    boolean existsByEmail (String email);
    
    boolean existsByUsernameAndPassword (String username, String password);
}