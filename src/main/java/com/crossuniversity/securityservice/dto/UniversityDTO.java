package com.crossuniversity.securityservice.dto;

import com.crossuniversity.securityservice.entity.University;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UniversityDTO {
    private Long id;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String domain;

    public static UniversityDTO parseEntityToDto(University university){
        return UniversityDTO.builder()
                .id(university.getId())
                .title(university.getTitle())
                .build();
    }
}
