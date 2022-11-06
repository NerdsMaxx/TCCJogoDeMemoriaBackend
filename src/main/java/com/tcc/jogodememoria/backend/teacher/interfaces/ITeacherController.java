package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.teacher.dtos.TeacherDto;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface ITeacherController {
  ResponseEntity<Object> saveTeacher(TeacherDto teacherDto);

  ResponseEntity<Object> getAllTeachers();

  ResponseEntity<Object> getATeacher(UUID id);

  ResponseEntity<Object> updateATeacher(UUID id, TeacherDto teacherDto);

  ResponseEntity<Object> deleteATeacher(UUID id);
}
