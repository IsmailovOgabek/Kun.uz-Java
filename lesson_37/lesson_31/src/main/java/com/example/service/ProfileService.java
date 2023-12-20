package com.example.service;

import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.exp.*;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.repository.ProfileRepository;
import com.example.repository.custom.ProfileCustomRepository;
import com.example.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepository profileCustomRepository;

    public ProfileDTO create(ProfileDTO profileDTO, Integer pId) {
        log.info("Create New Profile {} " + profileDTO);
        checkParameters(profileDTO);

        ProfileEntity entity = toEntity(profileDTO);
        entity.setPrtId(pId);

        profileRepository.save(entity);
        profileDTO.setId(entity.getId());
        return profileDTO;
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(profileDTO.getName());
        entity.setSurname(profileDTO.getSurname());
        entity.setPhone(profileDTO.getPhone());
        entity.setPassword(Md5Util.encode(profileDTO.getPassword()));
        entity.setRole(profileDTO.getRole());

        entity.setVisible(true);
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());

        return entity;
    }

    public Page<ProfileDTO> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProfileEntity> entities = profileRepository.findAll(pageable);

        List<ProfileEntity> content = entities.getContent();
        List<ProfileDTO> dto = new LinkedList<>();
        for (ProfileEntity profileEntity : content) {
            ProfileDTO profileDTO = toDTO(profileEntity);
            dto.add(profileDTO);
        }
        return new PageImpl<>(dto, pageable, entities.getTotalElements());
    }

    private ProfileDTO toDTO(ProfileEntity profileEntity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(profileEntity.getName());
        dto.setSurname(profileEntity.getSurname());
        dto.setRole(profileEntity.getRole());
        dto.setVisible(profileEntity.getVisible());

        return dto;
    }

    private void checkParameters(ProfileDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty() || dto.getName().trim().length() < 3) {
            throw new AppBadRequestException("Name Not Valid");
        }
        if (dto.getSurname() == null || dto.getSurname().trim().isEmpty() || dto.getSurname().trim().length() < 3) {
            throw new AppBadRequestException("Surname Not Valid");
        }
        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty() || dto.getPhone().trim().length() < 3) {
            throw new AppBadRequestException("Email Not Valid");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty() || dto.getPassword().trim().length() < 4) {
            throw new AppBadRequestException("Password Not Valid");
        }
        if (dto.getRole() == null) {
            throw new AppBadRequestException("Role Not Valid");
        }
    }

    public Boolean delete(Integer id) {
        profileRepository.deleteById(id);
        return true;
    }


    public ProfileDTO updateAdmin(Integer id, ProfileDTO profileDTO) {
        int b = profileRepository.updateAdminById(profileDTO.getName(), profileDTO.getSurname(), profileDTO.getVisible()
                , profileDTO.getRole(), profileDTO.getStatus(), id);

        if (b == 0) {
            throw new AppForbiddenException("Something went wrong");
        }
        profileDTO.setId(id);

        return profileDTO;
    }

    public int updateUser(Integer pId, ProfileDTO profileDTO) {
        int b = profileRepository.updateUserById(profileDTO.getName(), profileDTO.getSurname(),
                Md5Util.encode(profileDTO.getPassword()), pId);

        return b;
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            log.warn("Profile not found. id {}" + id);
            throw new ItemNotFoundException("Profile not exists");
        });
    }

    public void filter(ProfileFilterDTO filterDTO, int page, int size) {
        Page<ProfileEntity> pageObj = profileCustomRepository.filter(filterDTO, page, size);
        System.out.println(pageObj.getTotalElements());
        pageObj.forEach(entity -> {
            System.out.print(entity.getId() + " ");
            System.out.print(entity.getName() + " ");
            System.out.print(entity.getPhone() + " ");
            System.out.print(entity.getCreatedDate());
            System.out.println();
        });
    }
}
