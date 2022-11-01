package com.myapp.iam.dto;

import lombok.Data;

@Data
public class UserCreateRequestDto {
    private String username;
    private String email;
    private String password;
}
