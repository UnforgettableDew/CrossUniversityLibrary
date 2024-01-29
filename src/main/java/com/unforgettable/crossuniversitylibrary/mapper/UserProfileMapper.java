package com.unforgettable.crossuniversitylibrary.mapper;

import com.unforgettable.crossuniversitylibrary.dto.UserProfileDTO;
import com.unforgettable.crossuniversitylibrary.entity.UniversityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LibraryMapper.class)
public interface UserProfileMapper {
    @Mapping(source = "universityUser.userCredentials.email", target = "email")
    UserProfileDTO mapToDTO(UniversityUser universityUser);
}
