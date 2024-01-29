package com.unforgettable.crossuniversitylibrary.dto;

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
