package com.crossuniversity.securityservice.mapper;

import com.crossuniversity.securityservice.dto.DocumentDTO;
import com.crossuniversity.securityservice.entity.Document;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = BriefProfileMapper.class)
public interface DocumentMapper {
    DocumentDTO mapToDTO(Document document);

    List<DocumentDTO> mapListToDTO(List<Document> documents);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Document updateEntity(DocumentDTO documentDTO, @MappingTarget Document document);
}
