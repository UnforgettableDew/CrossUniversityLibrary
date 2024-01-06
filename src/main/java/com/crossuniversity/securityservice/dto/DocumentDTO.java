package com.crossuniversity.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "An object representing a document")
public class DocumentDTO {
    private Long id;

    private String title;

    private String topic;

    private String description;

    private UserBriefProfile owner;

}
