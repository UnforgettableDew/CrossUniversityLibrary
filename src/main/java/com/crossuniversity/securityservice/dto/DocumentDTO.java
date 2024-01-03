package com.crossuniversity.securityservice.dto;

import com.crossuniversity.securityservice.entity.Document;
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

    public static DocumentDTO parseEntityToDto(Document document){
        return DocumentDTO.builder()
                .id(document.getId())
                .title(document.getTitle())
                .topic(document.getTopic())
                .description(document.getDescription())
                .owner(UserBriefProfile.parseEntityToDto(document.getOwner()))
                .build();
    }
}
