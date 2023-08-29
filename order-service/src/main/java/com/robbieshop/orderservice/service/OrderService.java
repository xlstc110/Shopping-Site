package com.robbieshop.orderservice.service;

import com.robbieshop.orderservice.dto.OrderItemsDto;
import com.robbieshop.orderservice.dto.OrderRequest;
import com.robbieshop.orderservice.model.Order;
import com.robbieshop.orderservice.model.OrderItems;
import com.robbieshop.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor//Two tech stack: Lombok and Autowire: After Spring 4.3, single constructor method is no longer needed to have the @Autowire annotation, @RequiredArgsConstructor construct an implicit constructor.
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItems> orderItemsList = orderRequest.getOrderItemsDtos().stream()
                .map(orderItemsDto -> mapToDto(orderItemsDto))
                .toList();

        order.setOrderItemsList(orderItemsList);

        orderRepository.save(order);
    }

    public OrderItems mapToDto(OrderItemsDto orderItemsDto){
        return new OrderItems().builder()
                .price(orderItemsDto.getPrice())
                .skuCode(orderItemsDto.getSkuCode())
                .quantity(orderItemsDto.getQuantity())
                .build();
    }
}
