package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.teacher.dtos.TeacherDto;
import org.springframework.http.ResponseEntity;

public interface ITeacherController {
    ResponseEntity<Object> saveTeacher(TeacherDto teacherDto);

    ResponseEntity<Object> getAllTeachers();

    ResponseEntity<Object> getATeacher(Long id);

    ResponseEntity<Object> updateATeacher(Long id, TeacherDto teacherDto);

    ResponseEntity<Object> deleteATeacher(Long id);
}
