package com.unforgettable.crossuniversitylibrary.mapper;

import com.unforgettable.crossuniversitylibrary.dto.UniversityDTO;
import com.unforgettable.crossuniversitylibrary.entity.University;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversityMapper {
    UniversityDTO mapToDTO(University university);
}
