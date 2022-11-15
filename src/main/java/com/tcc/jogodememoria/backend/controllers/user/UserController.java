package com.tcc.jogodememoria.backend.controllers.user;

import com.tcc.jogodememoria.backend.dtos.UserDto;
import com.tcc.jogodememoria.backend.features.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import com.tcc.jogodememoria.backend.features.userType.interfaces.IUserTypeService;
import com.tcc.jogodememoria.backend.features.userType.models.UserTypeModel;
import com.tcc.jogodememoria.backend.responses.user.UserResponse;
import com.tcc.jogodememoria.backend.utils.CustomBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    final IUserService userServ;
    final IUserTypeService userTypeServ;
    
    public UserController (IUserService userServ, IUserTypeService userTypeServ) {
        this.userServ = userServ;
        this.userTypeServ = userTypeServ;
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<Object> save (@RequestBody @Valid UserDto userDto) {
        boolean existsByUsername = userServ.existsByUsername(userDto.getUsername());
        boolean existsByEmail = userServ.existsByEmail(userDto.getEmail());
        
        if ( existsByUsername && existsByEmail ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Usuário já está adicionado.");
        }
        
        if ( existsByUsername ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Usuário com este nome de usuário já existe.");
        }
        
        if ( existsByEmail ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Usuário com este e-mail já existe.");
        }
        
        final String type = userDto.getType();
        if ( type.compareToIgnoreCase("Professor") != 0 && type.compareToIgnoreCase("Aluno") != 0 ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
        }
        
        UserModel user = new UserModel();
        BeanUtils.copyProperties(userDto, user);
        
        // Na primeira vez que salvar, seta para conjunto de matérias vazias.
        user.setSubjects(new HashSet<>());
        
        UserTypeModel userType = new UserTypeModel(type);
        if ( ! userTypeServ.existsByType(type) ) {
            userType = userTypeServ.save(userType);
        } else {
            Optional<UserTypeModel> opUserType = userTypeServ.findByType(type);
            
            if ( opUserType.isEmpty() ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }
            
            userType = opUserType.get();
        }
        
        user.setUserType(userType);
        
        userServ.save(user);
        
        UserResponse userResp = new UserResponse();
        BeanUtils.copyProperties(user, userResp);
        userResp.setType(type);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userResp);
    }
    
    @PutMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> update (@PathVariable(value = "username") String username,
                                          @RequestBody UserDto userDto) {
        if ( CustomBeanUtils.isAllNullProperty(userDto) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Nenhum dado foi fornecido para professor.");
        }
        
        Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não existe usuário com este nome de usuário.");
        }
        
        UserModel user = opUser.get();
        
        if ( ! CustomBeanUtils.maybeHaveSomethingToPassToTarget(userDto, user) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Esta atualização já faz parte do usuário.");
        }
        
        CustomBeanUtils.copyNonNullProperties(userDto, user);
        if ( userDto.getType() != null ) {
            String type = userDto.getType();
            
            if ( type.compareToIgnoreCase("Aluno") != 0
                 && type.compareToIgnoreCase("Professor") != 0 ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
            }
            
            Optional<UserTypeModel> opUserType = userTypeServ.findByType(type);
            
            if ( opUserType.isEmpty() ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }
            
            user.setUserType(opUserType.get());
        }
        
        userServ.save(user);
        
        UserResponse userResp = new UserResponse();
        
        BeanUtils.copyProperties(user, userResp);
        
        userResp.setType(user.getUserType()
                             .getType());
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(userResp);
    }
    
    @GetMapping
    @Transactional
    public ResponseEntity<Object> getAll () {
        List<UserModel> users = userServ.findAll();
        
        if ( users.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Não existem usuários");
        }
        
        List<UserResponse> usersResp = users.stream()
                                            .map((user) -> {
                                                UserResponse userResp = new UserResponse();
                                                BeanUtils.copyProperties(user, userResp);
                                                userResp.setType(user.getUserType()
                                                                     .getType());
            
                                                return userResp;
                                            })
                                            .toList();
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(usersResp);
    }
    
    @GetMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> getByUsername (@PathVariable(value = "username") String username) {
        Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado usuário com este nome de usuário.");
        }
        
        UserModel user = opUser.get();
        UserResponse userResp = new UserResponse();
        
        BeanUtils.copyProperties(user, userResp);
        userResp.setType(user.getUserType()
                             .getType());
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(userResp);
    }
}
