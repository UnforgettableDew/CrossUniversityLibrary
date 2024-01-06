package com.crossuniversity.securityservice.mapper;

import com.crossuniversity.securityservice.dto.UserProfileDTO;
import com.crossuniversity.securityservice.entity.UniversityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LibraryMapper.class)
public interface UserProfileMapper {
    @Mapping(source = "universityUser.userCredentials.email", target = "email")
    UserProfileDTO mapToDTO(UniversityUser universityUser);
}
