package com.robbieshop.shoppingservice.controller;

import com.robbieshop.shoppingservice.dto.ProductRequest;
import com.robbieshop.shoppingservice.dto.ProductResponse;
import com.robbieshop.shoppingservice.model.Product;
import com.robbieshop.shoppingservice.repository.ProductRepository;
import com.robbieshop.shoppingservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//To avoid @ResponseBody in every endpoint.
//After 4.3, you can directly specify the URI path directly into the @RestController annotation without @RequestMapping.
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //Since the request to post a record into database, we are using a POST request.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)//Return the status of this request and its type to frontend
    //Match the request details to DTO file ProductRequest
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}
