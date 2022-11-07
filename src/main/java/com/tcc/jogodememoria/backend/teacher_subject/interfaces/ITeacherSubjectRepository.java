package com.tcc.jogodememoria.backend.teacher_subject.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;

@Repository
public interface ITeacherSubjectRepository
  extends JpaRepository<TeacherSubjectModel, UUID> {
  boolean existsByTeacherModelAndSubjectModel(
    TeacherModel teacherModel,
    SubjectModel subjectModel
  );

  Optional<TeacherSubjectModel> findByTeacherModelAndSubjectModel(
    TeacherModel teacherModel,
    SubjectModel subjectModel
  );
}
