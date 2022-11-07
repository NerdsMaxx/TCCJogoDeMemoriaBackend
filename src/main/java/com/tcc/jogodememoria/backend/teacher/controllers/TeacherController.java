package com.tcc.jogodememoria.backend.teacher.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.dtos.TeacherDto;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherController;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;

@RestController
@RequestMapping("/teacher")
public class TeacherController implements ITeacherController {

  private static final String TEACHER_NOT_FOUND_WARNING_STR = "Professor não foi encontrado.";

  public TeacherController(
      ITeacherService teacherService,
      ISubjectService subjectService) {
    this.teacherService = teacherService;
    this.subjectService = subjectService;
  }

  final ITeacherService teacherService;
  final ISubjectService subjectService;

  @Override
  @PostMapping
  public ResponseEntity<Object> saveTeacher(
      @RequestBody @Valid TeacherDto teacherDto) {
    if (teacherService.existsUserByEmail(teacherDto.getEmail())) {
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body("Professor já está adicionado.");
    }

    Optional<UserModel> optionalUserModel = teacherService.findUserByEmail(
        teacherDto.getEmail());

    if (!optionalUserModel.isPresent()) {
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body("Não existe usuário com este e-mail.");
    }

    TeacherModel teacherModel = new TeacherModel();
    teacherModel.setUserModel(optionalUserModel.get());
    teacherService.save(teacherModel);

    for (String subject : teacherDto.getSubjects()) {
      Optional<SubjectModel> optionalSubjectModel = teacherService.findSubjectModelByName(
          subject);

      if (optionalSubjectModel.isPresent()) {
        SubjectModel subjectModel = optionalSubjectModel.get();

        TeacherSubjectModel teacherSubjectModel = new TeacherSubjectModel();

        teacherSubjectModel.setTeacherModel(teacherModel);
        teacherSubjectModel.setSubjectModel(subjectModel);

        teacherService.saveTeacherSubjectModel(teacherSubjectModel);
      } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            "A matéria " + subject
                + " não existe. Deve adicionar matéria primeiro antes de adicionar"
                + " o professor com tal matéria.");

      }
    }

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("Professor adicionado com sucesso.");
  }

  @Override
  @GetMapping
  public ResponseEntity<Object> getAllTeachers() {
    return ResponseEntity.status(HttpStatus.OK).body(teacherService.findAll());
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<Object> getATeacher(
      @PathVariable(value = "id") UUID id) {
    Optional<TeacherModel> teacherModelOptional = teacherService.findById(id);

    if (!teacherModelOptional.isPresent()) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(TEACHER_NOT_FOUND_WARNING_STR);
    }

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(teacherModelOptional.get());
  }

  @Override
  public ResponseEntity<Object> updateATeacher(UUID id, TeacherDto teacherDto) {
    return null;
  }

  // @Override
  // @PutMapping("/{id}")
  // public ResponseEntity<Object> updateATeacher(
  // @PathVariable(value = "id") UUID id,
  // @RequestBody TeacherDto teacherDto
  // ) {
  // if (CustomBeanUtils.isAllNullProperty(teacherDto)) {
  // return ResponseEntity
  // .status(HttpStatus.CONFLICT)
  // .body("Nenhum dado foi fornecido para atualização do professor.");
  // }

  // Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

  // if (!optionalTeacherModel.isPresent()) {
  // return ResponseEntity
  // .status(HttpStatus.CONFLICT)
  // .body(TEACHER_NOT_FOUND_WARNING_STR);
  // }

  // TeacherModel teacherModel = new TeacherModel();
  // BeanUtils.copyProperties(optionalTeacherModel.get(), teacherModel);
  // CustomBeanUtils.copyNonNullProperties(teacherDto, teacherModel);

  // if (teacherDto.getEmail() != null) {
  // Optional<UserModel> optionalUserModel = teacherService.findUserByEmail(
  // teacherDto.getEmail()
  // );

  // if (!optionalUserModel.isPresent()) {
  // return ResponseEntity
  // .status(HttpStatus.CONFLICT)
  // .body("Não existe usuário com este e-mail.");
  // }

  // teacherModel.setUserModel(optionalUserModel.get());
  // }

  // teacherService.save(teacherModel);

  // return ResponseEntity
  // .status(HttpStatus.OK)
  // .body("Professor atualizado com sucesso.");
  // }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteATeacher(
      @PathVariable(value = "id") UUID id) {
    Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

    if (!optionalTeacherModel.isPresent()) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(TEACHER_NOT_FOUND_WARNING_STR);
    }

    teacherService.delete(optionalTeacherModel.get());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body("Usuário deletado com sucesso.");
  }
}
