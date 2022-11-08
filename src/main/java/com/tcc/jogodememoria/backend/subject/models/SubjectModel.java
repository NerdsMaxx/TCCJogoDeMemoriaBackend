package com.tcc.jogodememoria.backend.subject.models;

import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "table_subject")
public class SubjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String subjectName;

    @ManyToMany(mappedBy = "subjectModelSet", fetch = FetchType.EAGER)
    private Set<TeacherModel> teacherModelSet;
}
