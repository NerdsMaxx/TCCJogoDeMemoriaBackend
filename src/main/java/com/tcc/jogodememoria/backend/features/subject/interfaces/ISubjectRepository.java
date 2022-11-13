package com.tcc.jogodememoria.backend.features.subject.interfaces;

import com.tcc.jogodememoria.backend.features.subject.models.SubjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISubjectRepository extends JpaRepository<SubjectModel, UUID> {
    boolean existsByName(String name);

    Optional<SubjectModel> findByName(String name);
}
