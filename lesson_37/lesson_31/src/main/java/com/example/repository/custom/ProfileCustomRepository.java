package com.example.repository.custom;

import com.example.dto.profile.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProfileCustomRepository {
    @Autowired
    private EntityManager entityManager;


    public Page<ProfileEntity> filter(ProfileFilterDTO filter, int page, int size) {
        StringBuilder builder = new StringBuilder("SELECT a FROM ProfileEntity a ");
        StringBuilder countBuilder = new StringBuilder("select count(a) from ProfileEntity a ");
        Map<String, Object> params = new HashMap<>();

        if (filter.getVisible() != null) {
            builder.append(" where a.visible = ").append(filter.getVisible());
            countBuilder.append(" where a.visible = ").append(filter.getVisible());
        } else {
            builder.append(" where a.visible = true ");
            countBuilder.append(" where a.visible = true ");
        }

        if (filter.getName() != null) {
            builder.append(" And a.name =:name");
            countBuilder.append(" And a.name =:name");
            params.put("name", filter.getName());
        }

        if (filter.getSurname() != null) {
            builder.append(" And a.surname =:surname");
            countBuilder.append(" And a.surname =:surname");
            params.put("surname", filter.getSurname());
        }

        if (filter.getPhone() != null) {
            builder.append(" And a.phone =:phone");
            countBuilder.append(" And a.phone =:phone");
            params.put("phone", filter.getPhone());
        }

        if (filter.getEmail() != null) {
            builder.append(" And a.email =:email");
            countBuilder.append(" And a.email =:email");
            params.put("email", filter.getEmail());
        }

        if (filter.getStatus() != null) {
            builder.append(" And a.status =:status");
            countBuilder.append(" And a.status =:status");
            params.put("status", filter.getStatus());
        }

        if (filter.getProfileRole() != null) {
            builder.append(" And a.role =:role");
            countBuilder.append(" And a.role =:role");
            params.put("role", filter.getProfileRole());
        }

        if (filter.getFromDate() != null && filter.getToDate() != null) {
            builder.append(" And cast(a.createdDate as date) between :fromDate and :toDate ");
            params.put("fromDate", filter.getFromDate());
            params.put("toDate", filter.getToDate());
        } else if (filter.getFromDate() != null) { // from
            builder.append(" And a.createdDate > :fromDate ");
            params.put("fromDate", filter.getFromDate().atStartOfDay()); // 2022-12-09 00:00:000
        } else if (filter.getToDate() != null) {
            builder.append(" And a.createdDate < :toDate ");
            params.put("toDate", filter.getToDate().atTime(LocalTime.MAX)); // 2022-12-09 23:59:59.999999999
        }

        // content
        Query query = entityManager.createQuery(builder.toString());
        query.setFirstResult((page) * size); // 50
        query.setMaxResults(size); // 30

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<ProfileEntity> profileEntitieList = query.getResultList();

        // totalCount
       Query  countQuery = entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        Long totalElements = (Long) countQuery.getSingleResult();

        return new PageImpl<>(profileEntitieList, PageRequest.of(page, size), totalElements);
    }

    public List<ProfileEntity> getAll() {
        Query query = this.entityManager.createQuery("SELECT p From ProfileEntity as p");
        List profileEntities = query.getResultList();
        return profileEntities;
    }

    public List<ProfileEntity> getAllNative() {
        Query query = entityManager.createNativeQuery("SELECT * FROM profile ", ProfileEntity.class);
        List profileEntities = query.getResultList();
        return profileEntities;
    }


}
