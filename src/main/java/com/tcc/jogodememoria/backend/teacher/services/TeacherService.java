package com.tcc.jogodememoria.backend.teacher.services;

import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherRepository;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.interfaces.ITeacherSubjectService;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import com.tcc.jogodememoria.backend.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TeacherService implements ITeacherService {

  TeacherService(
    ITeacherRepository teacherRepository,
    IUserService userService,
    ITeacherSubjectService teacherSubjectService,
    ISubjectService subjectService
  ) {
    this.teacherRepository = teacherRepository;
    this.userService = userService;
    this.teacherSubjectService = teacherSubjectService;
    this.subjectService = subjectService;
  }

  final ITeacherRepository teacherRepository;
  final IUserService userService;
  final ITeacherSubjectService teacherSubjectService;
  final ISubjectService subjectService;

  @Override
  @Transactional
  public TeacherModel save(TeacherModel teacherModel) {
    return teacherRepository.save(teacherModel);
  }

  @Override
  @Transactional
  public TeacherSubjectModel saveTeacherSubjectModel(
    TeacherSubjectModel teacherSubjectModel
  ) {
    return teacherSubjectService.save(teacherSubjectModel);
  }

  @Override
  public boolean existsByUserModel(UserModel userModel) {
    return teacherRepository.existsByUserModel(userModel);
  }

  @Override
  public boolean existsUserByEmail(String email) {
    Optional<UserModel> optionalUserModel = userService.findByEmail(email);

    if (!optionalUserModel.isPresent()) {
      return false;
    }

    return existsByUserModel(optionalUserModel.get());
  }

  @Override
  public List<TeacherModel> findAll() {
    return teacherRepository.findAll();
  }

  @Override
  public Optional<TeacherModel> findById(UUID id) {
    return teacherRepository.findById(id);
  }

  @Override
  public Optional<TeacherModel> findByUserModel(UserModel userModel) {
    return teacherRepository.findByUserModel(userModel);
  }

  @Override
  public Optional<UserModel> findUserByEmail(String email) {
    return userService.findByEmail(email);
  }

  @Override
  public Optional<SubjectModel> findSubjectModelByName(String subjectName) {
    return subjectService.findBySubjectName(subjectName);
  }

  @Override
  @Transactional
  public void delete(TeacherModel teacherModel) {
    teacherRepository.delete(teacherModel);
  }
}
