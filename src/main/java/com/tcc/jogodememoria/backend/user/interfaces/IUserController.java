package com.tcc.jogodememoria.backend.user.interfaces;

import com.tcc.jogodememoria.backend.user.dtos.UserDtoWithPassword;
import org.springframework.http.ResponseEntity;

public interface IUserController {
    ResponseEntity<Object> saveUser(UserDtoWithPassword userDtoWithPassword);

    ResponseEntity<Object> getAllUsers();

    ResponseEntity<Object> getAUser(Long id);

    ResponseEntity<Object> updateAUser(Long id, UserDtoWithPassword userDtoUpdate);

    ResponseEntity<Object> deleteAUser(Long id);
}
