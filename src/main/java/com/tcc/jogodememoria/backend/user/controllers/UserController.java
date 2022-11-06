package com.tcc.jogodememoria.backend.user.controllers;

import com.tcc.jogodememoria.backend.user.dtos.UserDtoWithPassword;
import com.tcc.jogodememoria.backend.user.interfaces.IUserController;
import com.tcc.jogodememoria.backend.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import com.tcc.jogodememoria.backend.utils.CustomBeanUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

@RestController
@RequestMapping("/user")
public class UserController implements IUserController {

  static final String USER_NOT_FOUND_WARNING_STR =
    "Usuário não foi encontrado.";

  public UserController(IUserService userService) {
    this.userService = userService;
  }

  final IUserService userService;

  @Override
  @PostMapping
  public ResponseEntity<Object> saveUser(
    @RequestBody @Valid UserDtoWithPassword userDtoWithPassword
  ) {
    if (userService.existsByEmail(userDtoWithPassword.getEmail())) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Usuário já está adicionado.");
    }

    UserModel userModel = new UserModel();
    BeanUtils.copyProperties(userDtoWithPassword, userModel);
    userModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

    userService.save(userModel);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Usuário adicionado com sucesso.");
  }

  @Override
  @GetMapping
  public ResponseEntity<Object> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<Object> getAUser(@PathVariable(value = "id") UUID id) {
    Optional<UserModel> userModelOptional = userService.findById(id);

    if (!userModelOptional.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(USER_NOT_FOUND_WARNING_STR);
    }

    return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<Object> updateAUser(
    @PathVariable(value = "id") UUID id,
    @RequestBody UserDtoWithPassword userDtoWithPassword
  ) {
    if (CustomBeanUtils.isAllNullProperty(userDtoWithPassword)) {
      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Nenhum dado foi fornecido para atualização do usuário.");
    }

    Optional<UserModel> optionalUserModel = userService.findById(id);

    if (!optionalUserModel.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(USER_NOT_FOUND_WARNING_STR);
    }

    UserModel userModel = new UserModel();
    BeanUtils.copyProperties(optionalUserModel.get(), userModel);
    CustomBeanUtils.copyNonNullProperties(userDtoWithPassword, userModel);

    userService.save(userModel);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body("Usuário atualizado com sucesso.");
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteAUser(
    @PathVariable(value = "id") UUID id
  ) {
    Optional<UserModel> optionalUserModel = userService.findById(id);

    if (!optionalUserModel.isPresent()) {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(USER_NOT_FOUND_WARNING_STR);
    }

    userService.delete(optionalUserModel.get());

    return ResponseEntity
      .status(HttpStatus.OK)
      .body("Usuário deletado com sucesso.");
  }
}
