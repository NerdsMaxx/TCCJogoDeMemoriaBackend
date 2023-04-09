package com.tcc.app.web.memory_game.api.controllers;

import com.tcc.app.web.memory_game.api.dtos.requests.ResetPasswordRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
@CrossOrigin("*")
@AllArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    
    @PostMapping
    public ResponseEntity insertNewUser(@RequestBody @Valid UserRequestDto userRequestDto,
                                        UriComponentsBuilder uriBuilder) throws Exception {
        var user = userService.save(userRequestDto);
        
        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(user.getId()).toUri();
        
        return ResponseEntity.created(uri).body(userMapper.toUserResponseDto(user));
    }
    
    @PostMapping("/mudar-senha")
    public ResponseEntity changePassword(@RequestBody @Valid ResetPasswordRequestDto changePasswordRequestDto) {
        UserEntity user = userService.changePassword(changePasswordRequestDto);
        
        return ResponseEntity.ok(userMapper.toUserResponseDto(user));
    }
}