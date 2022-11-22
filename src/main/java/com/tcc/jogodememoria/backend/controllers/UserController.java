package com.tcc.jogodememoria.backend.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.jogodememoria.backend.dtos.user.UserDto;
import com.tcc.jogodememoria.backend.interfaces.services.IUserService;
import com.tcc.jogodememoria.backend.interfaces.services.IUserTypeService;
import com.tcc.jogodememoria.backend.models.UserModel;
import com.tcc.jogodememoria.backend.models.UserTypeModel;
import com.tcc.jogodememoria.backend.responses.user.UserResponse;
import com.tcc.jogodememoria.backend.utils.CustomBeanUtils;

@RestController
@RequestMapping("/user")
public class UserController {
    final IUserService userService;
    final IUserTypeService userTypeService;

    public UserController(final IUserService userService, final IUserTypeService userTypeService) {
        this.userService = userService;
        this.userTypeService = userTypeService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> save(@RequestBody @Valid final UserDto userDto) {
        final boolean existsByUsername = userService.existsByUsername(userDto.getUsername());
        final boolean existsByEmail = userService.existsByEmail(userDto.getEmail());

        if (existsByUsername && existsByEmail) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário já está adicionado.");
        }

        if (existsByUsername) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário com este nome de usuário já existe.");
        }

        if (existsByEmail) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário com este e-mail já existe.");
        }

        final String userTypeName = userDto.getType();
        if (!"Aluno".equalsIgnoreCase(userTypeName)
                && !"Professor".equalsIgnoreCase(userTypeName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
        }

        final UserModel user = new UserModel();
        BeanUtils.copyProperties(userDto, user);

        // Na primeira vez que salvar, seta para conjunto de matérias vazias.
        user.setSubjects(new HashSet<>());

        UserTypeModel userType = new UserTypeModel(userTypeName);
        if (!userTypeService.existsByType(userTypeName)) {
            userType = userTypeService.save(userType);
        } else {
            final Optional<UserTypeModel> opitionalUserType = userTypeService.findByType(userTypeName);

            if (opitionalUserType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }

            userType = opitionalUserType.get();
        }

        user.setUserType(userType);

        userService.save(user);

        final UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setType(userTypeName);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @PutMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> update(@PathVariable(value = "username") final String username,
            @RequestBody final UserDto userDto) {
        if (CustomBeanUtils.isAllNullProperty(userDto)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Nenhum dado foi fornecido para usuário.");
        }

        final Optional<UserModel> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não existe usuário com este nome de usuário.");
        }

        final UserModel user = optionalUser.get();

        if (!CustomBeanUtils.maybeHaveSomethingNewToPassToTarget(userDto, user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Esta atualização já faz parte do usuário.");
        }

        CustomBeanUtils.copyNonNullProperties(userDto, user);

        if (userDto.getType() != null) {
            final String userTypeString = userDto.getType();

            if (!"Aluno".equalsIgnoreCase(userTypeString)
                    && !"Professor".equalsIgnoreCase(userTypeString)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
            }

            final Optional<UserTypeModel> opitionalUserType = userTypeService.findByType(userTypeString);

            if (opitionalUserType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }

            user.setUserType(opitionalUserType.get());
        }

        userService.save(user);

        final UserResponse userResponse = new UserResponse();

        BeanUtils.copyProperties(user, userResponse);

        // .ff
        userResponse.setType(user.getUserType().getType());
        // .fo

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userResponse);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<Object> getAll() {
        final List<UserModel> userList = userService.findAll();

        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Não existem usuários");
        }
        // .ff
        final List<UserResponse> userResponseList = new ArrayList<>();
        for (final UserModel user : userList) {
            final UserResponse userResponse = new UserResponse();
            userResponse.setType(user.getUserType()
                    .getType());
            BeanUtils.copyProperties(user, userResponse);

            userResponseList.add(userResponse);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseList);
    }

    @GetMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> getByUsername(@PathVariable(value = "username") final String username) {
        final Optional<UserModel> opitionalUser = userService.findByUsername(username);

        if (opitionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado usuário com este nome de usuário.");
        }

        final UserModel user = opitionalUser.get();
        final UserResponse userResponse = new UserResponse();

        // .ff
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setType(user.getUserType().getType());
        // .fo

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponse);
    }

    @GetMapping("/{username}/{password}")
    @Transactional
    public ResponseEntity<Object> getBooleanByUsernameAndPassword(
            @PathVariable(value = "username") final String username,
            @PathVariable(value = "password") final String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Parâmetros incompletos.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.existsByUsernameAndPassword(username, password));
    }

    @DeleteMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> delete(@PathVariable(value = "username") final String username) {
        final Optional<UserModel> opitionalUser = userService.findByUsername(username);

        if (opitionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado usuário com este nome de usuário.");
        }

        userService.delete(opitionalUser.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body("Usuário foi deletado com sucesso.");
    }
}