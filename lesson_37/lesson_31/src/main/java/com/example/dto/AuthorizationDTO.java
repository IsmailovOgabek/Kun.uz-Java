package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationDTO {
    private String phone;
    private String password;

    @Override
    public String toString() {
        return "AuthorizationDTO{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
