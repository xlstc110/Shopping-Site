package com.robbieshop.inventoryservice.service;

import com.robbieshop.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        //check if the inventory object is present, if not, it means the inventory does not exist.
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}
