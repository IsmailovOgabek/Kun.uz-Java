package com.example.contoller;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.repository.ProfileRepository;
import com.example.service.ProfileService;
import com.example.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("/admin")
    public String init() {
        // checking
        if (profileRepository.findByPhone("7777") == null) {
            ProfileEntity entity = new ProfileEntity();
            entity.setName("Admin");
            entity.setSurname("Adminjon");
            entity.setPhone("7777");
            entity.setPassword(Md5Util.encode("7777"));
            entity.setRole(ProfileRole.ADMIN);
            entity.setStatus(ProfileStatus.ACTIVE);
            entity.setCreatedDate(LocalDateTime.now());
            entity.setVisible(Boolean.TRUE);
            profileRepository.save(entity);
            return "done";
        }
        return "Already exists";
    }

}
