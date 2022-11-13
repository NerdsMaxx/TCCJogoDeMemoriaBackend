package com.tcc.jogodememoria.backend.features.userType.interfaces;

import com.tcc.jogodememoria.backend.features.userType.models.UserTypeModel;
import com.tcc.jogodememoria.backend.interfaces.IService;

import java.util.Optional;

public interface IUserTypeService extends IService<UserTypeModel> {
    boolean existsByType(String type);

    Optional<UserTypeModel> findByType(String type);
}
