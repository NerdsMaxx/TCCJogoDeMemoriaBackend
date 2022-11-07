package com.tcc.jogodememoria.backend.subject.services;

import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectRepository;
import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements ISubjectService {

  SubjectService(ISubjectRepository subjectRepository) {
    this.subjectRepository = subjectRepository;
  }

  private final ISubjectRepository subjectRepository;

  @Override
  @Transactional
  public SubjectModel save(SubjectModel subjectModel) {
    return subjectRepository.save(subjectModel);
  }

  @Override
  public boolean existsById(UUID id) {
    return subjectRepository.existsById(id);
  }

  @Override
  public boolean existsBySubjectName(String subjectName) {
    return subjectRepository.existsBySubjectName(subjectName);
  }

  @Override
  public List<SubjectModel> findAll() {
    return subjectRepository.findAll();
  }

  @Override
  public Optional<SubjectModel> findById(UUID id) {
    return subjectRepository.findById(id);
  }

  @Override
  public Optional<SubjectModel> findBySubjectName(String subjectName) {
    return subjectRepository.findBySubjectName(subjectName);
  }

  @Override
  @Transactional
  public void delete(SubjectModel subjectModel) {
    subjectRepository.delete(subjectModel);
  }
}
