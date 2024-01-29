package com.unforgettable.crossuniversitylibrary.mapper;

import com.unforgettable.crossuniversitylibrary.dto.UserBriefProfile;
import com.unforgettable.crossuniversitylibrary.entity.UniversityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BriefProfileMapper {
    @Mapping(source = "universityUser.userCredentials.email", target = "email")
    UserBriefProfile mapToDTO(UniversityUser universityUser);

    List<UserBriefProfile> mapToListDTO(List<UniversityUser> universityUsers);
}
