package com.tcc.jogodememoria.backend.subject.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISubjectRepository extends JpaRepository<SubjectModel, UUID> {
  boolean existsBySubjectName(String subjectName);

  Optional<SubjectModel> findBySubjectName(String subjectName);
}
