package com.tcc.jogodememoria.backend.teacher_subject.primary_key;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class TeacherSubjectId implements Serializable {

    private static final long serialVersionUID = 1L;

    public TeacherSubjectId(UUID teacherId, UUID subjectId) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public TeacherSubjectId() {

    }

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private UUID subjectId;
}