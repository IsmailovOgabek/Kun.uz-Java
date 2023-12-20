package com.example.service;

import com.example.dto.AuthorizationDTO;
import com.example.dto.AuthorizationResponseDTO;
import com.example.dto.RegistrationDTO;
import com.example.dto.SmsVerifivationDTO;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.EmailAlreadyExistsException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import com.example.util.Md5Util;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private ResourceBundleService resourceService;

    public AuthorizationResponseDTO authorization(AuthorizationDTO dto, Language lang) {

        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndPassword(dto.getPhone(), Md5Util.encode(dto.getPassword()));
        if (optional.isEmpty()) {
            throw new AppBadRequestException(resourceService.getMessage("credential.wrong", lang.name()));
        }

        ProfileEntity profile = optional.get();
        if (!profile.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadRequestException("Profile Not Active");
        }

        AuthorizationResponseDTO respose = new AuthorizationResponseDTO();
        respose.setName(profile.getName());
        respose.setSurname(profile.getSurname());
        respose.setRole(profile.getRole());
        respose.setToken(JwtUtil.encode(profile.getId(), profile.getRole()));

        return respose;
    }

    public String registration(RegistrationDTO dto) {
        checkParameters(dto);

        ProfileEntity exists = profileRepository.findByPhone(dto.getPhone());
        if (exists != null) {
            if (exists.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRepository.delete(exists);
            } else {
                throw new EmailAlreadyExistsException("Phone already exists");
            }
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(Md5Util.encode(dto.getPassword()));
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setEmail(dto.getEmail());
        entity.setSmsCode("12345");
        profileRepository.save(entity);

       /* StringBuilder sb = new StringBuilder();
        sb.append("Salom Qalaysan \n");
        sb.append(" Bu test message");
        sb.append("Click there:  http://localhost:8080/auth/verification/email/").append(JwtUtil.encode(entity.getId()));
        emailService.sendEmail(dto.getEmail(), "Complite Registration", sb.toString());*/

       /* Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("<h1 style=\"text-align: center\">Salom Qalaysan</h1>");
                String link = String.format("<a href=\"http://localhost:8080/auth/verification/email/%s\"> Click there</a>", JwtUtil.encode(entity.getId()));
                sb.append(link);
                emailService.sendEmailMine(dto.getEmail(), "Complite Registration", sb.toString());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
*/

        /*smsService.sendSms(entity.getPhone(),"12345");*/
        return "Email ga link ketdi. Mazgi emailni tekshir.";
    }

    private void checkParameters(RegistrationDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank() || dto.getName().trim().length() <= 3) {
            throw new AppBadRequestException("Name is wrong");
        }
        if (dto.getSurname() == null || dto.getSurname().isBlank() || dto.getSurname().trim().length() <= 3) {
            throw new AppBadRequestException("Surname is wrong");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank() || dto.getPassword().trim().length() <= 3) {
            throw new AppBadRequestException("Password is wrong");
        }
        if (dto.getPhone() == null || dto.getPhone().isBlank() || dto.getPhone().trim().length() <= 3) {
            throw new AppBadRequestException("Phone is wrong");
        }
    }

    public String verification(String jwt) {
        Integer id;
        try {
            id = JwtUtil.decodeForEmailVerification(jwt);
        } catch (JwtException e) {
            return "Verification failed";
        }

        ProfileEntity exists = profileService.get(id);
        if (!exists.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            return "Verification failed";
        }
        exists.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(exists);

        return "Verification success";
    }

    public String phoneVerification(SmsVerifivationDTO dto) {
        // ...
        return null;
    }
}
