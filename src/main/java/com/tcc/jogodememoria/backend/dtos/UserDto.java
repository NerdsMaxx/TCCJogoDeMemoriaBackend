package com.tcc.jogodememoria.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String type;
}
