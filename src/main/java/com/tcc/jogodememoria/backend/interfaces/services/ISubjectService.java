package com.tcc.jogodememoria.backend.interfaces.services;

import com.tcc.jogodememoria.backend.interfaces.IService;
import com.tcc.jogodememoria.backend.models.SubjectModel;

import java.util.Optional;

public interface ISubjectService extends IService<SubjectModel> {
    boolean existsByName (String name);
    
    boolean notExistsByName (String name);
    
    Optional<SubjectModel> findByName (String name);
}