package com.tcc.jogodememoria.backend.teacher.controllers;

import com.tcc.jogodememoria.backend.teacher.dtos.TeacherDto;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

  public TeacherController(ITeacherService teacherService) {
    this.teacherService = teacherService;
  }

  final ITeacherService teacherService;

  @PostMapping
  public ResponseEntity<Object> saveTeacher(
    @RequestBody @Valid TeacherDto teacherDto
  ) {
    if (teacherService.existsUserByEmail(teacherDto.getEmail())) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Professor já está adicionado.");
    }

    Optional<UserModel> optionalUserModel = teacherService.findUserByEmail(
      teacherDto.getEmail()
    );

    if (!optionalUserModel.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("Não existe usuário com este e-mail.");
    }

    TeacherModel teacherModel = new TeacherModel();

    BeanUtils.copyProperties(teacherDto, teacherModel);
    teacherModel.setUser(optionalUserModel.get());

    teacherService.save(teacherModel);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Professor adicionado com sucesso.");
  }

  @GetMapping
  public ResponseEntity<Object> getAllTeachers() {
    return ResponseEntity.status(HttpStatus.OK).body(teacherService.findAll());
  }
  //   @GetMapping("/{id}")
  //   public ResponseEntity<Object> getAUser(@PathVariable(value = "id") UUID id) {
  //     Optional<TeacherModel> teacherModelOptional = teacherService.findById(id);

  //     if (!teacherModelOptional.isPresent()) {
  //       return ResponseEntity
  //         .status(HttpStatus.NOT_FOUND)
  //         .body(USER_NOT_FOUND_WARNING_STR);
  //     }

  //     return ResponseEntity.status(HttpStatus.OK).body(teacherModelOptional.get());
  //   }

  //   @PutMapping("/{id}")
  //   public ResponseEntity<Object> updateAUser(
  //     @PathVariable(value = "id") UUID id,
  //     @RequestBody UserDtoUpdate userDtoUpdate
  //   ) {
  //     if(CustomBeanUtils.isAllNullProperty(userDtoUpdate)){
  //       return ResponseEntity
  //         .status(HttpStatus.CONFLICT)
  //         .body("Nenhum dado foi fornecido para atualização do usuário.");
  //     }

  //     Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

  //     if (!optionalTeacherModel.isPresent()) {
  //       return ResponseEntity
  //         .status(HttpStatus.NOT_FOUND)
  //         .body(USER_NOT_FOUND_WARNING_STR);
  //     }

  //     TeacherModel teacherModel = new TeacherModel();
  //     BeanUtils.copyProperties(optionalTeacherModel.get(), teacherModel);
  //     CustomBeanUtils.copyNonNullProperties(userDtoUpdate, teacherModel);

  //     teacherService.save(teacherModel);

  //     return ResponseEntity
  //       .status(HttpStatus.OK)
  //       .body("Usuário atualizado com sucesso.");
  //   }

  //   @DeleteMapping("/{id}")
  //   public ResponseEntity<Object> deleteAUser(
  //     @PathVariable(value = "id") UUID id
  //   ) {
  //     Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

  //     if (!optionalTeacherModel.isPresent()) {
  //       return ResponseEntity
  //         .status(HttpStatus.NOT_FOUND)
  //         .body(USER_NOT_FOUND_WARNING_STR);
  //     }

  //     teacherService.delete(optionalTeacherModel.get());

  //     return ResponseEntity
  //       .status(HttpStatus.OK)
  //       .body("Usuário deletado com sucesso.");
  //   }
}
