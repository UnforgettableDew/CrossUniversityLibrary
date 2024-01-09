package com.crossuniversity.securityservice.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryDTO {
    private Long id;

    private String title;

    private String topic;

    private boolean libraryAccess;
}
