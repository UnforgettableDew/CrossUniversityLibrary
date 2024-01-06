package com.crossuniversity.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "An object representing a library")
public class LibraryDTO {
    @Schema(example = "3")
    private Long id;

    @Schema(example = "library_title")
    private String title;

    @Schema(example = "library_topic")
    private String topic;

    @Schema(example = "false")
    private boolean libraryAccess;
}
