package com.tcc.jogodememoria.backend.controllers;

import com.tcc.jogodememoria.backend.dtos.UserDto;
import com.tcc.jogodememoria.backend.features.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import com.tcc.jogodememoria.backend.features.userType.interfaces.IUserTypeService;
import com.tcc.jogodememoria.backend.features.userType.models.UserTypeModel;
import com.tcc.jogodememoria.backend.responses.user.UserSavedResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    public UserController(IUserService userServ, IUserTypeService userTypeServ) {
        this.userServ = userServ;
        this.userTypeServ = userTypeServ;
    }

    final IUserService userServ;
    final IUserTypeService userTypeServ;

    @PostMapping
    @Transactional
    public ResponseEntity<Object> saveUser(
            @RequestBody @Valid UserDto userDto
    ) {
        boolean existsByUsername = userServ.existsByUsername(userDto.getUsername());
        boolean existsByEmail = userServ.existsByEmail(userDto.getEmail());

        if (existsByUsername && existsByEmail) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Usuário já está adicionado.");
        }

        if (existsByUsername) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Usuário com este nome de usuário já existe.");
        }

        if (existsByEmail) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Usuário com este e-mail já existe.");
        }

        final String type = userDto.getType();
        if (type.compareToIgnoreCase("Professor") != 0
                && type.compareToIgnoreCase("Aluno") != 0) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
        }

        UserModel user = new UserModel();
        BeanUtils.copyProperties(userDto, user);

        //Na primeira vez que salvar, seta para conjunto de matérias vazias.
        user.setSubjects(new HashSet<>());

        UserTypeModel userType = new UserTypeModel(type);
        if (!userTypeServ.existsByType(type)) {
            userType = userTypeServ.save(userType);
        } else {
            Optional<UserTypeModel> opUserType = userTypeServ.findByType(type);

            if (opUserType.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }

            userType = opUserType.get();
        }

        user.setUserType(userType);

        userServ.save(user);

        UserSavedResponse userResp = new UserSavedResponse();
        BeanUtils.copyProperties(user, userResp);
        userResp.setType(type);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResp);
    }


}
