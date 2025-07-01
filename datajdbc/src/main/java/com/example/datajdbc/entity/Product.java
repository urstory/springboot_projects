package com.example.datajdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("products")
public class Product {
    @Id
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String category;
    private Boolean inStock;

    public Product(String name, Double price, String description, String category, Boolean inStock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.inStock = inStock;
    }
} 