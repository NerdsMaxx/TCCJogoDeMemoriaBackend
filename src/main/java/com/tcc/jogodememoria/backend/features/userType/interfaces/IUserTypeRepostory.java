package com.tcc.jogodememoria.backend.features.userType.interfaces;

import com.tcc.jogodememoria.backend.features.userType.models.UserTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserTypeRepostory extends JpaRepository<UserTypeModel, UUID> {
    boolean existsByType(String type);

    Optional<UserTypeModel> findByType(String type);
}
