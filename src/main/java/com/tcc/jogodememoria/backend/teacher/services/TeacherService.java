package com.tcc.jogodememoria.backend.teacher.services;

import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherRepository;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
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
    IUserService userService
  ) {
    this.teacherRepository = teacherRepository;
    this.userService = userService;
  }

  final ITeacherRepository teacherRepository;
  final IUserService userService;

  @Transactional
  public TeacherModel save(TeacherModel teacherModel) {
    return teacherRepository.save(teacherModel);
  }

  public boolean existsByUser(UserModel userModel) {
    return teacherRepository.existsByUser(userModel);
  }

  public boolean existsUserByEmail(String email) {
    Optional<UserModel> optionalUserModel = userService.findByEmail(email);

    if (!optionalUserModel.isPresent()) {
      return false;
    }

    return existsByUser(optionalUserModel.get());
  }

  public List<TeacherModel> findAll() {
    return teacherRepository.findAll();
  }

  public Optional<TeacherModel> findById(UUID id) {
    return teacherRepository.findById(id);
  }

  public Optional<TeacherModel> findByUser(UserModel userModel) {
    return teacherRepository.findByUser(userModel);
  }

  public Optional<UserModel> findUserByEmail(String email) {
    return userService.findByEmail(email);
  }

  @Transactional
  public void delete(TeacherModel teacherModel) {
    teacherRepository.delete(teacherModel);
  }
}
