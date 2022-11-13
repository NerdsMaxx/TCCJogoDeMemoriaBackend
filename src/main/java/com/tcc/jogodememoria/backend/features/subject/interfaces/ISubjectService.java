package com.tcc.jogodememoria.backend.features.subject.interfaces;

import com.tcc.jogodememoria.backend.features.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.interfaces.IService;

import java.util.Optional;

public interface ISubjectService extends IService<SubjectModel> {
    boolean existsByName(String name);

    boolean notExistsByName(String name);

    Optional<SubjectModel> findByName(String name);
}
