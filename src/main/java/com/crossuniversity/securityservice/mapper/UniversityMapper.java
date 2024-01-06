package com.crossuniversity.securityservice.mapper;

import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.entity.University;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversityMapper {
    UniversityDTO mapToDTO(University university);
}
