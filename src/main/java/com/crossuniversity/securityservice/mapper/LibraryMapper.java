package com.crossuniversity.securityservice.mapper;

import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.entity.Library;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Library updateEntity(LibraryDTO libraryDTO, @MappingTarget Library library);

    LibraryDTO mapToDTO(Library library);

    Library mapToEntity(LibraryDTO libraryDTO);

    List<LibraryDTO> mapToListDTO(List<Library> libraries);
}
