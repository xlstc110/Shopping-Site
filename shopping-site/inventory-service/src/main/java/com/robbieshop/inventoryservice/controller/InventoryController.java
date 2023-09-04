package com.robbieshop.inventoryservice.controller;

import com.robbieshop.inventoryservice.dto.InventoryResponse;
//import com.robbieshop.inventoryservice.repository.InventoryRepository;
import com.robbieshop.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    //@PathVariable format URL: http://localhost:8082/api/inventory/iphone 15,iphone 14
    //@RequestParam format URL: http://localhost:8082/api/inventory?skuCode=iphone 15&skuCode=iphone 14
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){

        return inventoryService.isInStock(skuCode);
    }
}
