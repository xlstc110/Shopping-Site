package com.robbieshop.shoppingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")//MongoDB's annotation to indicate this is a Document model
@AllArgsConstructor
@NoArgsConstructor
@Builder//Stream way to create a new Object of this class with fields.
@Data//getter and setter.
public class Product {
    @Id//Unique Identifier for Product
    private String id;
    private String name;
    private String description;
    private BigDecimal price;//Use BigDecimal to prevent round-off errors that might occur when using Float and double.
}
