package com.tcc.jogodememoria.backend.teacher_subject.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;

import java.util.List;
import java.util.Optional;

public interface ITeacherSubjectService {
    TeacherSubjectModel save(TeacherSubjectModel teacherSubjectModel);

    boolean existsByTeacherModelAndSubjectModel(
            TeacherModel teacherModel,
            SubjectModel subjectModel
    );

    List<TeacherSubjectModel> findAll();

    Optional<TeacherSubjectModel> findByTeacherModelAndSubjectModel(
            TeacherModel teacherModel,
            SubjectModel subjectModel
    );

    void delete(TeacherSubjectModel teacherSubjectModel);
}
