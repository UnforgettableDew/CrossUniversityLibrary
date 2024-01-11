package com.crossuniversity.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadedFile {
    private String fileName;
    private ByteArrayResource resource;
}
