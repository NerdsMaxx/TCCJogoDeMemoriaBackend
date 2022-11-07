package com.tcc.jogodememoria.backend.subject.interfaces;

import com.tcc.jogodememoria.backend.subject.dtos.SubjectDto;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface ISubjectController {
  ResponseEntity<Object> saveSubject(SubjectDto subjectDto);

  ResponseEntity<Object> getAllSubjects();

  ResponseEntity<Object> getASubject(UUID id);

  ResponseEntity<Object> updateASubject(UUID id, SubjectDto subjectDto);

  ResponseEntity<Object> deleteASubject(UUID id);
}
