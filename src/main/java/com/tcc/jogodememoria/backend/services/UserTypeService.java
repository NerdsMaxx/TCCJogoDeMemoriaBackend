package com.tcc.jogodememoria.backend.services;

import com.tcc.jogodememoria.backend.interfaces.repositories.IUserTypeRepostory;
import com.tcc.jogodememoria.backend.interfaces.services.IUserTypeService;
import com.tcc.jogodememoria.backend.models.UserTypeModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTypeService implements IUserTypeService {
    public UserTypeService (IUserTypeRepostory userRepo) {
        this.userRepo = userRepo;
    }
    
    final IUserTypeRepostory userRepo;
    
    @Override
    public boolean existsByType (String type) {
        return userRepo.existsByType(type);
    }
    
    @Override
    public Optional<UserTypeModel> findByType (String type) {
        return userRepo.findByType(type);
    }
    
    @Override
    public UserTypeModel save (UserTypeModel userType) {
        return userRepo.save(userType);
    }
    
    @Override
    public void delete (UserTypeModel userType) {
        userRepo.delete(userType);
    }
}