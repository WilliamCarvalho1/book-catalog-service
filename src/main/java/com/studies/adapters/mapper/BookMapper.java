package com.studies.adapters.mapper;

import com.studies.adapters.entity.BookEntity;
import com.studies.domain.dto.BookDTO;

import java.util.List;

public class BookMapper {

    private BookMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BookEntity mapToEntity(BookDTO dto) {
        return BookEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .author(dto.getAuthor())
                .category(dto.getCategory())
                .status(dto.getStatus())
                .price(dto.getPrice())
                .build();
    }

    public static BookDTO mapToDTO(BookEntity entity) {
        return BookDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .author(entity.getAuthor())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .price(entity.getPrice())
                .build();
    }

    public static List<BookDTO> mapToDTOList(List<BookEntity> entityList) {
        return entityList.stream()
                .map(BookMapper::mapToDTO)
                .toList();
    }

}
