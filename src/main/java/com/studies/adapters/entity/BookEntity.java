package com.studies.adapters.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@Data
@Builder
//@Entity
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "books")
//public class BookEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//    private String name;
//    private String author;
//    private String category;
//    private String status;
//    private double price;
//}

@Document(collection = "books")
public class BookEntity {

    @Id
    private String id;
    private String name;
    private String author;
    private String category;
    private String status;
    private double price;
}