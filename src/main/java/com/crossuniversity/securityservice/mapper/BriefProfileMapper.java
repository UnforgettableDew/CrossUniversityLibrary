package com.crossuniversity.securityservice.mapper;

import com.crossuniversity.securityservice.dto.UserBriefProfile;
import com.crossuniversity.securityservice.entity.UniversityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BriefProfileMapper {
    @Mapping(source = "universityUser.userCredentials.email", target = "email")
    UserBriefProfile mapToDTO(UniversityUser universityUser);
}
