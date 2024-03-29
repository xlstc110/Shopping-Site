package com.robbieshop.orderservice.service;

import com.robbieshop.orderservice.dto.InventoryResponse;
import com.robbieshop.orderservice.dto.OrderItemsDto;
import com.robbieshop.orderservice.dto.OrderRequest;
import com.robbieshop.orderservice.event.OrderPlacedEvent;
import com.robbieshop.orderservice.model.Order;
import com.robbieshop.orderservice.model.OrderItems;
import com.robbieshop.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final Tracer tracer;
    //The KafkaTemplate require a Key and Value pair as the Topic and Message relationship(Dictionary)
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

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

        //Since the postOrder API endpoint return a CompletableFuture Async type, creating unique Span for this Request span chain
        // is necessary, because Async call creates a seperated thread and the default tracing is not tracing the new thread.
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");//Store the written variable

        //
        try(Tracer.SpanInScope isSpanInScope = tracer.withSpan(inventoryServiceLookup.start())){
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

                Span notificationServiceLookup = tracer.nextSpan().name("notificationServiceLookup");

                //try resources
                try(Tracer.SpanInScope isSpanInScope1 = tracer.withSpan(notificationServiceLookup.start())){
                    //After the order is placed, send a Topic name and the order number as the Message to the message queue.
                    //In this case, the message can be the order.getOrderNumber or create an OrderPlacedEvent class to store and send as JSON message.
                    kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                } finally {
                    notificationServiceLookup.end();
                }

                return "order placed!";
            }
            else {
                throw new IllegalArgumentException("Product is in sufficient.");
            }

        } finally {
            inventoryServiceLookup.end();
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
