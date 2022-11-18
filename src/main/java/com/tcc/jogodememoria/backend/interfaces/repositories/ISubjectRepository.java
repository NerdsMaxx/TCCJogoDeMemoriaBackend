package com.tcc.jogodememoria.backend.interfaces.repositories;

import com.tcc.jogodememoria.backend.models.SubjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISubjectRepository extends JpaRepository<SubjectModel, UUID> {
    boolean existsByName (String name);
    
    Optional<SubjectModel> findByName (String name);
}