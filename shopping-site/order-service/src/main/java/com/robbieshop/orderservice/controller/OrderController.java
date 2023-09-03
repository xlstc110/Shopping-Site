package com.robbieshop.orderservice.controller;

import com.robbieshop.orderservice.dto.OrderRequest;
import com.robbieshop.orderservice.model.Order;
import com.robbieshop.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @TimeLimiter(name = "inventory")//Set the timeout limitation to the method, the "name" attribute much match the resilience4J configuration in properties file, ex: .instances.inventory
    @Retry(name = "inventory")//The Retry annotation will take the Retry configuration in properties file and execute the retry attempts.
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")//the calls made in this method will apply the Circuit Breaker pattern, give the name to indicate which method is the fallbackMethod to call.
    //create a fallback method when the request service is in Open state, the method needs to have the same return and parameter type as the original method
    public CompletableFuture<String> postOrder(@RequestBody OrderRequest orderRequest){
        //A CompletableFuture represents a future result of an asynchronous computation; you can write code that depends on the result, but you don't have to block and wait for the computation to finish.
        //Now the orderService.palceOrder() method will run a seperated thread(not main), whenever the running time is executed over 3 second, a timeout exception will throw out.
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    //if the inventory service is not working, a runtime exception will pop since it happened in runtime, so the fallback method needs the same exception method to return.
    public CompletableFuture<String> fallbackMethod(@RequestBody OrderRequest orderRequest, RuntimeException runtimeException){
        //return a stateless message to the user(customer) instead of the runtime error detail
        return CompletableFuture.supplyAsync(() -> "Ops, something went wrong! Please place your order in another time");
    }

}
