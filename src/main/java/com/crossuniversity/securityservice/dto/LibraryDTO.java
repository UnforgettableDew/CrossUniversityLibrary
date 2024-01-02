package com.crossuniversity.securityservice.dto;

import com.crossuniversity.securityservice.entity.Library;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryDTO {
    private Long id;
    private String title;
    private String topic;
    private boolean libraryAccess;

    public static LibraryDTO parseEntityToDto(Library library){
        return LibraryDTO.builder()
                .title(library.getTitle())
                .topic(library.getTopic())
                .libraryAccess(library.isLibraryAccess())
                .build();
    }
}
