package com.example.dto;

import com.example.enums.ProfileRole;
import com.example.repository.ProfileRepository;

public class JwtDTO {
    private Integer id;
    private ProfileRole role;

    public JwtDTO(Integer id, ProfileRole role) {
        this.id = id;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public ProfileRole getRole() {
        return role;
    }
}
