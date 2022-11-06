package com.tcc.jogodememoria.backend.subject.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import java.util.Optional;
import java.util.UUID;

public interface ISubjectService {
  SubjectModel save(SubjectModel subject);

  boolean existsById(UUID id);

  boolean existsBySubjectAndTeacher(
    String subjectName,
    TeacherModel teacherModel
  );

  Optional<SubjectModel> findById(UUID id);

  Optional<SubjectModel> findBySubjectAndTeacher(
    String subjectName,
    TeacherModel teacherModel
  );

  void delete(SubjectModel subject);
}
