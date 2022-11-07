package com.tcc.jogodememoria.backend.teacher_subject.models;

import com.tcc.jogodememoria.backend.teacher_subject.primary_key.TeacherSubjectId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "table_teacher_subject")
public class TeacherSubjectModel {

    @EmbeddedId
    private TeacherSubjectId teacherSubjectId;
}