package com.myprojects.userservice.service;

import com.myprojects.userservice.entity.UserProfileEntity;
import com.myprojects.userservice.repository.UserProfileRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileEntity createUser(UserProfileEntity user){
        return userProfileRepository.save(user);
    }
}
