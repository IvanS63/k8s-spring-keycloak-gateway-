package com.myprojects.userservice.controller;

import com.myprojects.userservice.api.UsersApi;
import com.myprojects.userservice.model.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    @Override
    public ResponseEntity<Void> deleteUserProfile(String userId) {
        return UsersApi.super.deleteUserProfile(userId);
    }

    @Override
    public ResponseEntity<UserProfileDto> getUserProfile(String userId) {
        return UsersApi.super.getUserProfile(userId);
    }

    @Override
    public ResponseEntity<Void> userSignUp(UserProfileDto userProfileDto) {
        return UsersApi.super.userSignUp(userProfileDto);
    }
}
