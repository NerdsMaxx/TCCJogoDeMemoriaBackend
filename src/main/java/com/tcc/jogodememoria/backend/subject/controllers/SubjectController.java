package com.tcc.jogodememoria.backend.subject.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.jogodememoria.backend.subject.dtos.SubjectDto;
import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectController;
import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;

@RestController
@RequestMapping("/subject")
public class SubjectController implements ISubjectController {

  public SubjectController(ISubjectService subjectService) {
    this.subjectService = subjectService;
  }

  private final ISubjectService subjectService;

  @Override
  @PostMapping
  public ResponseEntity<Object> saveSubject(
    @RequestBody @Valid SubjectDto subjectDto
  ) {
    if (subjectService.existsBySubjectName(subjectDto.getSubjectName())) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("A matéria já está adicionado.");
    }

    SubjectModel subjectModel = new SubjectModel();
    BeanUtils.copyProperties(subjectDto, subjectModel);

    subjectService.save(subjectModel);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Matéria adicionado com sucesso.");
  }

  @Override
  @GetMapping
  public ResponseEntity<Object> getAllSubjects() {
    return ResponseEntity.status(HttpStatus.OK).body(subjectService.findAll());
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<Object> getASubject(
    @PathVariable(value = "id") UUID id
  ) {
    Optional<SubjectModel> optionalSubejctModel = subjectService.findById(id);

    if (!optionalSubejctModel.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Não foi encontrado a matéria.");
    }

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(optionalSubejctModel.get());
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<Object> updateASubject(
    @PathVariable(value = "id") UUID id,
    @RequestBody @Valid SubjectDto subjectDto
  ) {
    Optional<SubjectModel> optionalSubejctModel = subjectService.findById(id);

    if (!optionalSubejctModel.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Não foi encontrado a matéria.");
    }

    SubjectModel subjectModel = new SubjectModel();
    BeanUtils.copyProperties(optionalSubejctModel.get(), subjectModel);
    BeanUtils.copyProperties(subjectDto, subjectModel);

    subjectService.save(subjectModel);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body("Matéria atualizado com sucesso.");
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteASubject(UUID id) {
    Optional<SubjectModel> optionalSubejctModel = subjectService.findById(id);

    if (!optionalSubejctModel.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Não foi encontrado a matéria.");
    }

    subjectService.delete(optionalSubejctModel.get());

    return ResponseEntity
      .status(HttpStatus.OK)
      .body("Matéria deletada com sucesso.");
  }
}
