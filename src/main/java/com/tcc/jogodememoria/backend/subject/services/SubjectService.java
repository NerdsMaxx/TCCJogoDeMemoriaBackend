package com.tcc.jogodememoria.backend.subject.services;

import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectRepository;
import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements ISubjectService {

  SubjectService(ISubjectRepository subjectRepository) {
    this.subjectRepository = subjectRepository;
  }

  private final ISubjectRepository subjectRepository;

  @Override
  @Transactional
  public SubjectModel save(SubjectModel subject) {
    return subjectRepository.save(subject);
  }

  @Override
  public boolean existsById(UUID id) {
    return subjectRepository.existsById(id);
  }

  @Override
  public boolean existsBySubjectAndTeacher(
    String subjectName,
    TeacherModel teacherModel
  ) {
    return subjectRepository.existsBySubjectAndTeacher(
      subjectName,
      teacherModel
    );
  }

  @Override
  public Optional<SubjectModel> findById(UUID id) {
    return subjectRepository.findById(id);
  }

  @Override
  public Optional<SubjectModel> findBySubjectAndTeacher(
    String subjectName,
    TeacherModel teacherModel
  ) {
    return subjectRepository.findBySubjectAndTeacher(subjectName, teacherModel);
  }

  @Override
  @Transactional
  public void delete(SubjectModel subject) {
    subjectRepository.delete(subject);
  }
}
