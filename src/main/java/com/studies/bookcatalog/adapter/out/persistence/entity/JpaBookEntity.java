package com.studies.bookcatalog.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class JpaBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;
    @Column(name = "author", nullable = false)
    public String author;
    @Column(name = "category", nullable = false)
    public String category;
    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    public BigDecimal price;
    @Column(name = "quantity", nullable = false)
    public int quantity;
}
