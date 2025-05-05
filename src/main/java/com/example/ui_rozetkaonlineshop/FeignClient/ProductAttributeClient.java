package com.example.ui_rozetkaonlineshop.FeignClient;

import com.example.ui_rozetkaonlineshop.DTO.ProductAttribute.ProductAttributeClientDTO;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "PRODUCT",
        contextId = "productAttributeClient",
        configuration = FeignConfig.class
)
public interface ProductAttributeClient {
    @GetMapping("/api/v1/attributes/product/{productId}")
    ResponseEntity<ProductAttributeClientDTO.ListResponse> getProductAttributes(@PathVariable Long productId);

    @GetMapping("/api/v1/attributes/{attributeId}")
    ResponseEntity<ProductAttributeClientDTO.Response> getAttributeById(@PathVariable Long attributeId);

    @PostMapping("/api/v1/attributes")
    ResponseEntity<ProductAttributeClientDTO.Response> createAttribute(
            @RequestBody ProductAttributeClientDTO.CreateRequest createDTO);

    @PostMapping("/api/v1/attributes/batch/{productId}")
    ResponseEntity<List<ProductAttributeClientDTO.Response>> createAttributes(
            @PathVariable Long productId,
            @RequestBody List<ProductAttributeClientDTO.CreateRequest> createDTOs);

    @PutMapping("/api/v1/attributes/{attributeId}")
    ResponseEntity<ProductAttributeClientDTO.Response> updateAttribute(
            @PathVariable Long attributeId,
            @RequestBody ProductAttributeClientDTO.UpdateRequest updateDTO);

    @DeleteMapping("/api/v1/attributes/{attributeId}")
    ResponseEntity<Void> deleteAttribute(@PathVariable Long attributeId);

    @DeleteMapping("/api/v1/attributes/product/{productId}")
    ResponseEntity<Void> deleteAllProductAttributes(@PathVariable Long productId);

    @GetMapping("/api/v1/attributes/search/color/{color}")
    ResponseEntity<List<ProductAttributeClientDTO.Response>> findAttributesByColor(@PathVariable String color);

    @GetMapping("/api/v1/attributes/search/size/{size}")
    ResponseEntity<List<ProductAttributeClientDTO.Response>> findAttributesBySize(@PathVariable String size);

    @GetMapping("/api/v1/attributes/search/material/{material}")
    ResponseEntity<List<ProductAttributeClientDTO.Response>> findAttributesByMaterial(@PathVariable String material);
}
