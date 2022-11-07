package com.tcc.jogodememoria.backend.teacher_subject.services;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.interfaces.ITeacherSubjectRepository;
import com.tcc.jogodememoria.backend.teacher_subject.interfaces.ITeacherSubjectService;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class TeacherSubjectService implements ITeacherSubjectService {

  TeacherSubjectService(ITeacherSubjectRepository teacherSubjectRepository) {
    this.teacherSubjectRepository = teacherSubjectRepository;
  }

  private final ITeacherSubjectRepository teacherSubjectRepository;

  @Transactional
  @Override
  public TeacherSubjectModel save(TeacherSubjectModel teacherSubjectModel) {
    return teacherSubjectRepository.save(teacherSubjectModel);
  }

  @Override
  public boolean existsById(UUID id) {
    return teacherSubjectRepository.existsById(id);
  }

  @Override
  public boolean existsByTeacherModelAndSubjectModel(
    TeacherModel teacherModel,
    SubjectModel subjectModel
  ) {
    return teacherSubjectRepository.existsByTeacherModelAndSubjectModel(
      teacherModel,
      subjectModel
    );
  }

  @Override
  public List<TeacherSubjectModel> findAll() {
    return teacherSubjectRepository.findAll();
  }

  @Override
  public Optional<TeacherSubjectModel> findById(UUID id) {
    return teacherSubjectRepository.findById(id);
  }

  @Override
  public Optional<TeacherSubjectModel> findByTeacherModelAndSubjectModel(
    TeacherModel teacherModel,
    SubjectModel subjectModel
  ) {
    return teacherSubjectRepository.findByTeacherModelAndSubjectModel(
      teacherModel,
      subjectModel
    );
  }

  @Transactional
  @Override
  public void delete(TeacherSubjectModel teacherSubjectModel) {
    teacherSubjectRepository.delete(teacherSubjectModel);
  }
}
