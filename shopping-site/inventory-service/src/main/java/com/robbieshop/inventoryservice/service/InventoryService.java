package com.robbieshop.inventoryservice.service;

import com.robbieshop.inventoryservice.dto.InventoryResponse;
import com.robbieshop.inventoryservice.model.Inventory;
import com.robbieshop.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows//ignore the exception, used to test in develop environment, never use in production environment.
    public List<InventoryResponse> isInStock(List<String> skuCode){
        //log the info of when did the request start and end to prosecute.
        log.info("wait time started");
        //Force the main thread to pause for 10 second to test the timeout circuit breaker function.
        Thread.sleep(10000);
        log.info("wait time ended");

        //check if the inventory object is present, if not, it means the inventory does not exist.
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isStock(inventory.getQuantity() > 0)
                            .build()
                )
                .toList();
    }

//    public InventoryResponse mapToInventoryDto(Inventory inventory){
//        return new InventoryResponse().builder()
//                .quantity(inventory.getQuantity())
//                .skuCode(inventory.getSkuCode())
//                .build();
//    }
}
