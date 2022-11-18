package com.tcc.jogodememoria.backend.interfaces.repositories;

import com.tcc.jogodememoria.backend.models.UserTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserTypeRepostory extends JpaRepository<UserTypeModel, UUID> {
    boolean existsByType (String type);
    
    Optional<UserTypeModel> findByType (String type);
}