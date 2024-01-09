package com.crossuniversity.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DocumentDTO {
    private Long id;

    private String title;

    private String topic;

    private String description;

    private UserBriefProfile owner;

}
