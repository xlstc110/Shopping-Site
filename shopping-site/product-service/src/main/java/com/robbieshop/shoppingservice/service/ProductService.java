package com.robbieshop.shoppingservice.service;

import com.robbieshop.shoppingservice.dto.ProductRequest;
import com.robbieshop.shoppingservice.dto.ProductResponse;
import com.robbieshop.shoppingservice.model.Product;
import com.robbieshop.shoppingservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor//While the compile time, it will generate all required contructor arguments
@Slf4j//Logging tool from Lombok
public class ProductService {

    //Dependency injection for product repository
    private final ProductRepository productRepository;

//    public ProductService(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();//create type Product

        productRepository.save(product);
        //Slf4j can make use of a placeholder like C#:
        log.info("Prodcut {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        //Use stream() to perform mapping action
        //The .toList() is a terminal operation that collects the output into a new list.
        return products.stream().map(product -> responseMapping(product)).toList();
    }

    public ProductResponse responseMapping(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
