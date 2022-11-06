package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITeacherService {
  TeacherModel save(TeacherModel teacherModel);

  boolean existsByUser(UserModel userModel);

  boolean existsUserByEmail(String email);

  List<TeacherModel> findAll();

  Optional<TeacherModel> findById(UUID id);

  Optional<TeacherModel> findByUser(UserModel userModel);

  Optional<UserModel> findUserByEmail(String email);

  void delete(TeacherModel teacherModel);
}
