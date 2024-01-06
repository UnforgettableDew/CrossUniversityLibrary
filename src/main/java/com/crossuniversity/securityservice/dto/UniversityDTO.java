package com.crossuniversity.securityservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "An object representing a university")
public class UniversityDTO {
    private Long id;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String domain;
}
