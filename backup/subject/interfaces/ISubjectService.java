package com.tcc.jogodememoria.backend.subject.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;

import java.util.List;
import java.util.Optional;

public interface ISubjectService {
    SubjectModel saveSubjectModel(SubjectModel subjectModel);

    boolean existsById(Long id);

    boolean existsBySubjectName(String subjectName);

    List<SubjectModel> findAll();

    Optional<SubjectModel> findById(Long id);

    Optional<SubjectModel> findBySubjectName(String subjectName);

    void deleteSubjectModel(SubjectModel subjectModel);
}
