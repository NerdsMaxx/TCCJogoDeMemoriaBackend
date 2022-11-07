package com.tcc.jogodememoria.backend.subject.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;

public interface ISubjectService {
  SubjectModel save(SubjectModel subjectModel);

  boolean existsById(UUID id);

  boolean existsBySubjectName(String subjectName);

  List<SubjectModel> findAll();

  Optional<SubjectModel> findById(UUID id);

  Optional<SubjectModel> findBySubjectName(String subjectName);

  void delete(SubjectModel subjectModel);
}
