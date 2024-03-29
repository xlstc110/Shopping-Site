package com.robbieshop.notificationservice;

import com.robbieshop.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")//the name of order service class after order placed
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        //send out email notification logic
        log.info("Received notification for order - {}", orderPlacedEvent.getOrderNumber());
    }
}
