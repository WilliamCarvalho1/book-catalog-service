package com.studies.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDTO {
    private int id;
    private String name;
    private String author;
    private String category;
    private String status;
    private double price;
}
