package com.crossuniversity.securityservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LibraryDTO> ownLibraries;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LibraryDTO> subscribedLibraries;

}
