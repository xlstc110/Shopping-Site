package com.robbieshop.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity//Entity annotation for SQL database
@Table(name = "t_orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderNumber;
    @OneToMany(cascade = CascadeType.ALL)//Each order can contain multiple items
    private List<OrderItems> orderItemsList;


}
