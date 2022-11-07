package com.tcc.jogodememoria.backend.teacher_subject.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;

public interface ITeacherSubjectService {
  TeacherSubjectModel save(TeacherSubjectModel teacherSubjectModel);

  boolean existsById(UUID id);

  boolean existsByTeacherModelAndSubjectModel(
    TeacherModel teacherModel,
    SubjectModel subjectModel
  );

  List<TeacherSubjectModel> findAll();

  Optional<TeacherSubjectModel> findById(UUID id);

  Optional<TeacherSubjectModel> findByTeacherModelAndSubjectModel(
    TeacherModel teacherModel,
    SubjectModel subjectModel
  );

  void delete(TeacherSubjectModel teacherSubjectModel);
}
