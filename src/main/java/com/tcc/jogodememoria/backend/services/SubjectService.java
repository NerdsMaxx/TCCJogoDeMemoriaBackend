package com.tcc.jogodememoria.backend.services;

import com.tcc.jogodememoria.backend.interfaces.repositories.ISubjectRepository;
import com.tcc.jogodememoria.backend.interfaces.services.ISubjectService;
import com.tcc.jogodememoria.backend.models.SubjectModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectService implements ISubjectService {
    final ISubjectRepository subjectRepo;
    
    public SubjectService (ISubjectRepository subjectRepo) {
        this.subjectRepo = subjectRepo;
    }
    
    @Override
    public boolean existsByName (String name) {
        return subjectRepo.existsByName(name);
    }
    
    @Override
    public boolean notExistsByName (String name) {
        return ! existsByName(name);
    }
    
    @Override
    public Optional<SubjectModel> findByName (String name) {
        return subjectRepo.findByName(name);
    }
    
    @Override
    public SubjectModel save (SubjectModel subject) {
        return subjectRepo.save(subject);
    }
    
    @Override
    public void delete (SubjectModel subject) {
        subjectRepo.delete(subject);
    }
}