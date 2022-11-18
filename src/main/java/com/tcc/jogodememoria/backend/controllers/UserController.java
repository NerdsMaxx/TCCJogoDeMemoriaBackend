package com.tcc.jogodememoria.backend.controllers;

import com.tcc.jogodememoria.backend.dtos.UserDto;
import com.tcc.jogodememoria.backend.interfaces.services.IUserService;
import com.tcc.jogodememoria.backend.interfaces.services.IUserTypeService;
import com.tcc.jogodememoria.backend.models.UserModel;
import com.tcc.jogodememoria.backend.models.UserTypeModel;
import com.tcc.jogodememoria.backend.responses.user.UserResponse;
import com.tcc.jogodememoria.backend.utils.CustomBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    final IUserService userServ;
    final IUserTypeService userTypeServ;
    
    public UserController (final IUserService userServ, final IUserTypeService userTypeServ) {
        this.userServ = userServ;
        this.userTypeServ = userTypeServ;
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<Object> save (@RequestBody @Valid final UserDto userDto) {
        final boolean existsByUsername = userServ.existsByUsername(userDto.getUsername());
        final boolean existsByEmail = userServ.existsByEmail(userDto.getEmail());
        
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
        if ( ! type.equalsIgnoreCase("Aluno") && ! type.equalsIgnoreCase("Professor") ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
        }
        
        final UserModel user = new UserModel();
        BeanUtils.copyProperties(userDto, user);
        
        // Na primeira vez que salvar, seta para conjunto de matérias vazias.
        user.setSubjects(new HashSet<>());
        
        UserTypeModel userType = new UserTypeModel(type);
        if ( ! userTypeServ.existsByType(type) ) {
            userType = userTypeServ.save(userType);
        } else {
            final Optional<UserTypeModel> opUserType = userTypeServ.findByType(type);
            
            if ( opUserType.isEmpty() ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }
            
            userType = opUserType.get();
        }
        
        user.setUserType(userType);
        
        userServ.save(user);
        
        final UserResponse userResp = new UserResponse();
        BeanUtils.copyProperties(user, userResp);
        userResp.setType(type);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userResp);
    }
    
    @PutMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> update (@PathVariable(value = "username") final String username,
                                          @RequestBody final UserDto userDto) {
        if ( CustomBeanUtils.isAllNullProperty(userDto) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Nenhum dado foi fornecido para professor.");
        }
        
        final Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não existe usuário com este nome de usuário.");
        }
        
        final UserModel user = opUser.get();
        
        if ( ! CustomBeanUtils.maybeHaveSomethingToPassToTarget(userDto, user) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Esta atualização já faz parte do usuário.");
        }
        
        CustomBeanUtils.copyNonNullProperties(userDto, user);
        
        if ( userDto.getType() != null ) {
            String type = userDto.getType();
            
            if ( ! type.equalsIgnoreCase("Aluno")
                 && ! type.equalsIgnoreCase("Professor") ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Tipo de usuário não válido. Só pode ser professor ou aluno.");
            }
            
            final Optional<UserTypeModel> opUserType = userTypeServ.findByType(type);
            
            if ( opUserType.isEmpty() ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Por algum motivo não foi encontrado o tipo do usuário.");
            }
            
            user.setUserType(opUserType.get());
        }
        
        userServ.save(user);
        
        final UserResponse userResp = new UserResponse();
        
        BeanUtils.copyProperties(user, userResp);
        
        //.ff
        userResp.setType( user.getUserType().getType() );
        //.fo
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(userResp);
    }
    
    @GetMapping
    @Transactional
    public ResponseEntity<Object> getAll () {
        final List<UserModel> users = userServ.findAll();
        
        if ( users.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Não existem usuários");
        }
        
        final List<UserResponse> usersResp = new ArrayList<>();
        for ( final UserModel user : users ) {
            final UserResponse userResp = new UserResponse();
            userResp.setType(user.getUserType()
                                 .getType());
            BeanUtils.copyProperties(user, userResp);
            
            usersResp.add(userResp);
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(usersResp);
    }
    
    @GetMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> getByUsername (@PathVariable(value = "username") final String username) {
        final Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado usuário com este nome de usuário.");
        }
        
        final UserModel user = opUser.get();
        final UserResponse userResp = new UserResponse();
        
        //.ff
		BeanUtils.copyProperties( user, userResp );
		userResp.setType( user.getUserType().getType() );
		//.fo
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(userResp);
    }
    
    @GetMapping("/{username}/{password}")
    @Transactional
    public ResponseEntity<Object> getBooleanByUsernameAndPassword (
            @PathVariable(value = "username") final String username,
            @PathVariable(value = "password") final String password) {
        if ( username.isEmpty() || password.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Parâmetros incompletos.");
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(userServ.existsByUsernameAndPassword(username, password));
    }
    
    @DeleteMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> delete (@PathVariable(value = "username") final String username) {
        final Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado usuário com este nome de usuário.");
        }
        
        userServ.delete(opUser.get());
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body("Usuário foi deletado com sucesso.");
    }
}