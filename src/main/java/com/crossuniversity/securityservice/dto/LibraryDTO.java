package com.crossuniversity.securityservice.dto;

import com.crossuniversity.securityservice.entity.Library;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "An object representing a library")
public class LibraryDTO {
    @Schema(example = "3", description = "asfafdasdfasd")
    private Long id;

    @Schema(example = "library_title")
    private String title;

    @Schema(example = "library_topic")
    private String topic;

    @Schema(example = "false")
    private boolean libraryAccess;

    public static LibraryDTO parseEntityToDto(Library library){
        return LibraryDTO.builder()
                .id(library.getId())
                .title(library.getTitle())
                .topic(library.getTopic())
                .libraryAccess(library.isLibraryAccess())
                .build();
    }
}
