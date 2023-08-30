package com.robbieshop.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder Builder(){
        //this will create a bean of type WebClient and name it as the method name,
        //in this case, the WebClient bean's name in the spring container is "webClient"
        return WebClient.builder();
    }

}
