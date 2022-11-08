package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;

import java.util.List;
import java.util.Optional;

public interface ITeacherService {
    TeacherModel saveTeacherModel(TeacherModel teacherModel);

    boolean existsByUserModel(UserModel userModel);

    boolean existsUserByEmail(String email);

    boolean existsSubjectModelByName(String subjectName);

    List<TeacherModel> findAll();

    Optional<TeacherModel> findById(Long id);

    Optional<TeacherModel> findByUserModel(UserModel userModel);

    Optional<UserModel> findUserByEmail(String email);

    Optional<SubjectModel> findSubjectModelByName(String subjectName);

    void deleteTeacherModel(TeacherModel teacherModel);
}
