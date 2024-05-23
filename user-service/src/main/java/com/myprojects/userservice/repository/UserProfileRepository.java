package com.myprojects.userservice.repository;

import com.myprojects.userservice.entity.UserProfileEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
    boolean existsByEmailIgnoreCase(String email);
}
