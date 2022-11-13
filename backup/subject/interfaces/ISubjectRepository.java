package com.tcc.jogodememoria.backend.subject.interfaces;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubjectRepository extends JpaRepository<SubjectModel, Long> {
    boolean existsBySubjectName(String subjectName);

    Optional<SubjectModel> findBySubjectName(String subjectName);
}
