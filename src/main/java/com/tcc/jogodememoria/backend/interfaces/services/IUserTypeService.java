package com.tcc.jogodememoria.backend.interfaces.services;

import com.tcc.jogodememoria.backend.interfaces.IService;
import com.tcc.jogodememoria.backend.models.UserTypeModel;

import java.util.Optional;

public interface IUserTypeService extends IService<UserTypeModel> {
    boolean existsByType (String type);
    
    Optional<UserTypeModel> findByType (String type);
}