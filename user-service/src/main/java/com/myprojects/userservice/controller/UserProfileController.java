package com.myprojects.userservice.controller;

import com.myprojects.userservice.api.UsersApi;
import com.myprojects.userservice.entity.UserProfileEntity;
import com.myprojects.userservice.mapper.UserProfileMapper;
import com.myprojects.userservice.model.UserProfileResponseDto;
import com.myprojects.userservice.model.UserSignUpRequestDto;
import com.myprojects.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserProfileController implements UsersApi {

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public UserProfileResponseDto userSignUp(UserSignUpRequestDto userSignUpRequestDto) {
        UserProfileEntity entity = userProfileService.createUser(userProfileMapper.toEntity(userSignUpRequestDto), userSignUpRequestDto.getPassword());
        return userProfileMapper.toDto(entity);
    }

    @Override
    public void deleteUserProfile(String userId) {
        userProfileService.deleteUser(userId);
    }

    @Override
    public UserProfileResponseDto getUserProfile(String userId) {
        return userProfileMapper.toDto(userProfileService.getUserProfile(userId));
    }
}
