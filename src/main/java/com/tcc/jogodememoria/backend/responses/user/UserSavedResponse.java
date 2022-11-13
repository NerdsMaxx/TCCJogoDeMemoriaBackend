package com.tcc.jogodememoria.backend.responses.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSavedResponse {
    private String name;

    private String username;

    private String email;

    private String type;
}
