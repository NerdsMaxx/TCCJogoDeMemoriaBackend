package com.tcc.jogodememoria.backend.subject.models;

import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class SubjectModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private String subjectName;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private TeacherModel teacherModel;
}
