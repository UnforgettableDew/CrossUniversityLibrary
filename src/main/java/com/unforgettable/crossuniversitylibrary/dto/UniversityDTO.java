package com.unforgettable.crossuniversitylibrary.dto;

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
}
