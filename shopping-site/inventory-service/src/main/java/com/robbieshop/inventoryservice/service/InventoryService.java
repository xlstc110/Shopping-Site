package com.robbieshop.inventoryservice.service;

import com.robbieshop.inventoryservice.dto.InventoryResponse;
import com.robbieshop.inventoryservice.model.Inventory;
import com.robbieshop.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
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
