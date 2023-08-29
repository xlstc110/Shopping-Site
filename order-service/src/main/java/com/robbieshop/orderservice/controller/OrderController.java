package com.robbieshop.orderservice.controller;

import com.robbieshop.orderservice.dto.OrderRequest;
import com.robbieshop.orderservice.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @PostMapping
    public String postOrder(@RequestBody OrderRequest orderRequest){
        return "Order Placed";
    }

}
