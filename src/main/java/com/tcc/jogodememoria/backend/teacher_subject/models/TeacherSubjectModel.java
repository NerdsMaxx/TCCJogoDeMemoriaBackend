package com.tcc.jogodememoria.backend.teacher_subject.models;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "table_teacher_subject")
public class TeacherSubjectModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private TeacherModel teacherModel;

  @ManyToOne
  @JoinColumn(name = "subject_id")
  private SubjectModel subjectModel;
}

// @Embeddable
// class TeacherSubjectId implements Serializable {
//   TeacherModel teacherModel;
//   SubjectModel subjectModel;
// }