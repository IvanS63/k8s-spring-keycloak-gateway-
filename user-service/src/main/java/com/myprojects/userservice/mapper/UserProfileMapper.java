package com.myprojects.userservice.mapper;

import com.myprojects.userservice.entity.UserProfileEntity;
import com.myprojects.userservice.model.UserProfileResponseDto;
import com.myprojects.userservice.model.UserSignUpRequestDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    UserProfileEntity toEntity(UserSignUpRequestDto dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    UserProfileResponseDto toDto(UserProfileEntity entity);
}
