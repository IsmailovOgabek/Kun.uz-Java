package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
    ProfileEntity findByPhone(String phone);

    Optional<ProfileEntity> findByPhoneAndPassword(String phone, String password);


    @Transactional
    @Modifying
    @Query("update ProfileEntity set name = ?1, surname = ?2 , visible = ?3 , role = ?4 , status = ?5 where id = ?6")
    int updateAdminById(String name, String surname, Boolean visible, ProfileRole role, ProfileStatus status, Integer id);


    @Transactional
    @Modifying
    @Query("update ProfileEntity set name = ?1, surname = ?2 , password = ?3  where id = ?4")
    int updateUserById(String name, String surname, String password, Integer id);

}



