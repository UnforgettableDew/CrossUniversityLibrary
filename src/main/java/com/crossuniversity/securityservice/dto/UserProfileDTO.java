package com.crossuniversity.securityservice.dto;

import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String userName;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double space;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UniversityDTO university;

    private List<LibraryDTO> ownLibraries;

    public static UserProfileDTO parseEntityToDto(UniversityUser universityUser){
        UniversityDTO universityDTO = UniversityDTO.parseEntityToDto(universityUser.getUniversity());
        List<LibraryDTO> ownLibrariesDTO = universityUser.getOwnLibraries()
                .stream().map(LibraryDTO::parseEntityToDto).toList();

        return UserProfileDTO.builder()
                .id(universityUser.getId())
                .userName(universityUser.getUserName())
                .university(universityDTO)
                .email(universityUser.getUserCredentials().getEmail())
                .space(universityUser.getSpace())
                .ownLibraries(ownLibrariesDTO)
                .build();
    }
}
