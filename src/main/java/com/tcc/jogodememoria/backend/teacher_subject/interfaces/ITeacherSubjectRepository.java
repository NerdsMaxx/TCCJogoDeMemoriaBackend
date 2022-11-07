package com.tcc.jogodememoria.backend.teacher_subject.interfaces;

import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import com.tcc.jogodememoria.backend.teacher_subject.primary_key.TeacherSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITeacherSubjectRepository
        extends JpaRepository<TeacherSubjectModel, TeacherSubjectId> {
}
