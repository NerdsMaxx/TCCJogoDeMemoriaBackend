package com.tcc.jogodememoria.backend.user.interfaces;

import com.tcc.jogodememoria.backend.user.dtos.UserDtoUpdate;
import com.tcc.jogodememoria.backend.user.dtos.UserDtoWithPassword;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface IUserController {
  ResponseEntity<Object> saveUser(UserDtoWithPassword userDtoWithPassword);

  ResponseEntity<Object> getAllUsers();

  ResponseEntity<Object> getAUser(UUID id);

  ResponseEntity<Object> updateAUser(UUID id, UserDtoUpdate userDtoUpdate);

  ResponseEntity<Object> deleteAUser(UUID id);
}
