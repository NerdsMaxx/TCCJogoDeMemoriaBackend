package com.tcc.jogodememoria.backend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "USER_TYPE")
public class UserTypeModel {
    
    public UserTypeModel () {
    }
    
    public UserTypeModel (String type) {
        this.type = type;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String type;
    
    @OneToMany(mappedBy = "userType", fetch = FetchType.EAGER)
    private Set<UserModel> userSet;
}