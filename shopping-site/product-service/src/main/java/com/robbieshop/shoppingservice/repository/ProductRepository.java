package com.robbieshop.shoppingservice.repository;

import com.robbieshop.shoppingservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

//extends MongoRepository interface to use built-in methods or customer methods
//passing in generic argument Product and identifier argument String since the ID field in Product class is String type
public interface ProductRepository extends MongoRepository<Product, String> {
}
