package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITeacherService {
    TeacherModel save(TeacherModel teacherModel);

    TeacherSubjectModel saveTeacherSubjectModel(TeacherSubjectModel teacherSubjectModel);

    boolean existsByUserModel(UserModel userModel);

    boolean existsUserByEmail(String email);

    boolean existsTeacherSubjectModelByTeacherModelAndSubjectModel(TeacherModel teacherModel, SubjectModel subjectModel);

    List<TeacherModel> findAll();

    Optional<TeacherModel> findById(UUID id);

    Optional<TeacherModel> findByUserModel(UserModel userModel);

    Optional<UserModel> findUserByEmail(String email);

    Optional<SubjectModel> findSubjectModelByName(String subjectName);

    Optional<TeacherSubjectModel> findTeacherSubjectModelByTeacherSubejctID(TeacherModel teacherModel, SubjectModel subjectModel);

    void deleteTeacherSubjectModel(TeacherSubjectModel teacherSubjectModel);

    void delete(TeacherModel teacherModel);
}
