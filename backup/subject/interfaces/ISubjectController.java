package com.tcc.jogodememoria.backend.subject.interfaces;

import com.tcc.jogodememoria.backend.subject.dtos.SubjectDto;
import org.springframework.http.ResponseEntity;

public interface ISubjectController {
    ResponseEntity<Object> saveSubject(SubjectDto subjectDto);

    ResponseEntity<Object> getAllSubjects();

    ResponseEntity<Object> getASubject(Long id);

    ResponseEntity<Object> updateASubject(Long id, SubjectDto subjectDto);

    ResponseEntity<Object> deleteASubject(Long id);
}
