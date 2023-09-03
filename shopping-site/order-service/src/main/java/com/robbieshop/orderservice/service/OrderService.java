package com.robbieshop.orderservice.service;

import com.robbieshop.orderservice.dto.InventoryResponse;
import com.robbieshop.orderservice.dto.OrderItemsDto;
import com.robbieshop.orderservice.dto.OrderRequest;
import com.robbieshop.orderservice.model.Order;
import com.robbieshop.orderservice.model.OrderItems;
import com.robbieshop.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor//Two tech stack: Lombok and Autowire: After Spring 4.3, single constructor method is no longer needed to have the @Autowire annotation, @RequiredArgsConstructor construct an implicit constructor.
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItems> orderItemsList = orderRequest.getOrderItemsDtos().stream()
                .map(orderItemsDto -> mapToDto(orderItemsDto))
                .toList();

        order.setOrderItemsList(orderItemsList);

        //Create a List<String> that contains all skuCode that the order required.
        List<String> skuCodes = order.getOrderItemsList().stream()
                .map(orderItems -> orderItems.getSkuCode())
                .toList();

        //Call inventory service and place order if the inventory is in stock.
        //WebClient can make HTTP call to the server.
        //Indicate the URL of the service to send the HTTP request
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                            .uri("http://inventory-service/api/inventory",
                                    uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                            .retrieve()
                            .bodyToMono(InventoryResponse[].class)
                            .block();
        //The WebClient by default will make an Asynchronous call, using .block() method to make the Synchronous call.

        //Since inventoryResponses is an Array, not a List<> we have to use Arrays.stream() method to use stream feature:
        boolean inStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.getIsStock());

        if(inStock) {
            orderRepository.save(order);
            return "order placed!";
        }
        else {
            throw new IllegalArgumentException("Product is in sufficient.");
        }
    }

    public OrderItems mapToDto(OrderItemsDto orderItemsDto){
        return new OrderItems().builder()
                .price(orderItemsDto.getPrice())
                .skuCode(orderItemsDto.getSkuCode())
                .quantity(orderItemsDto.getQuantity())
                .build();
    }
}
