package com.tcc.jogodememoria.backend.responses.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private String name;
    
    private String username;
    
    private String email;
    
    private String type;
}
