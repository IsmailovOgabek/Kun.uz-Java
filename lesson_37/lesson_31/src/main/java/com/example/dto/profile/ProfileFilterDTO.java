package com.example.dto.profile;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileFilterDTO {
    private Integer id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private LocalDate fromDate;
    private LocalDate toDate;

    private ProfileStatus status;
    private ProfileRole profileRole;
    private Boolean visible;
}
