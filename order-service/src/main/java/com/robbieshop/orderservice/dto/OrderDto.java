package com.robbieshop.orderservice.dto;

import com.robbieshop.orderservice.model.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String orderNumber;
    private List<OrderItems> orderItemsList;
}
