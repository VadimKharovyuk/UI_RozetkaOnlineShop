package com.example.ui_rozetkaonlineshop.service;

import com.example.ui_rozetkaonlineshop.DTO.ProductAttribute.ProductAttributeClientDTO;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductAttributeClient;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductAttributeClientService {

    private final ProductAttributeClient productServiceClient;

    public ProductAttributeClientDTO.ListResponse getProductAttributes(Long productId) {
        try {
            ResponseEntity<ProductAttributeClientDTO.ListResponse> response =
                    productServiceClient.getProductAttributes(productId);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching product attributes for productId: {}", productId, e);
            return createEmptyListResponse(productId);
        }
    }

    public ProductAttributeClientDTO.Response getAttributeById(Long attributeId) {
        try {
            ResponseEntity<ProductAttributeClientDTO.Response> response =
                    productServiceClient.getAttributeById(attributeId);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching attribute with id: {}", attributeId, e);
            return null;
        }
    }

    public ProductAttributeClientDTO.Response createAttribute(ProductAttributeClientDTO.CreateRequest request) {
        try {
            ResponseEntity<ProductAttributeClientDTO.Response> response =
                    productServiceClient.createAttribute(request);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error creating attribute: {}", request, e);
            return null;
        }
    }

    public List<ProductAttributeClientDTO.Response> createAttributes(
            Long productId, List<ProductAttributeClientDTO.CreateRequest> requests) {
        try {
            ResponseEntity<List<ProductAttributeClientDTO.Response>> response =
                    productServiceClient.createAttributes(productId, requests);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error creating multiple attributes for productId: {}", productId, e);
            return Collections.emptyList();
        }
    }

    public ProductAttributeClientDTO.Response updateAttribute(
            Long attributeId, ProductAttributeClientDTO.UpdateRequest request) {
        try {
            ResponseEntity<ProductAttributeClientDTO.Response> response =
                    productServiceClient.updateAttribute(attributeId, request);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error updating attribute with id: {}", attributeId, e);
            return null;
        }
    }

    public boolean deleteAttribute(Long attributeId) {
        try {
            ResponseEntity<Void> response = productServiceClient.deleteAttribute(attributeId);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error deleting attribute with id: {}", attributeId, e);
            return false;
        }
    }

    public boolean deleteAllProductAttributes(Long productId) {
        try {
            ResponseEntity<Void> response = productServiceClient.deleteAllProductAttributes(productId);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error deleting all attributes for productId: {}", productId, e);
            return false;
        }
    }

    public List<ProductAttributeClientDTO.Response> findAttributesByColor(String color) {
        try {
            ResponseEntity<List<ProductAttributeClientDTO.Response>> response =
                    productServiceClient.findAttributesByColor(color);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error finding attributes by color: {}", color, e);
            return Collections.emptyList();
        }
    }

    public List<ProductAttributeClientDTO.Response> findAttributesBySize(String size) {
        try {
            ResponseEntity<List<ProductAttributeClientDTO.Response>> response =
                    productServiceClient.findAttributesBySize(size);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error finding attributes by size: {}", size, e);
            return Collections.emptyList();
        }
    }

    public List<ProductAttributeClientDTO.Response> findAttributesByMaterial(String material) {
        try {
            ResponseEntity<List<ProductAttributeClientDTO.Response>> response =
                    productServiceClient.findAttributesByMaterial(material);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error finding attributes by material: {}", material, e);
            return Collections.emptyList();
        }
    }

    private ProductAttributeClientDTO.ListResponse createEmptyListResponse(Long productId) {
        return ProductAttributeClientDTO.ListResponse.builder()
                .productId(productId)
                .attributes(Collections.emptyList())
                .build();
    }
}