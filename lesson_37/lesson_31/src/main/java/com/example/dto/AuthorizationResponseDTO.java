package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorizationResponseDTO {

    private String name;
    private String surname;
    private ProfileRole role;
    private String token;


}
