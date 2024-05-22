package com.myprojects.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {
    @Id
    private UUID id;

    private String keycloakId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime creationDate;

    public UserProfileEntity() {
    }
}
