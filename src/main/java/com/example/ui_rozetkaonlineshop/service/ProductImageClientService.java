package com.example.ui_rozetkaonlineshop.service;


import com.example.ui_rozetkaonlineshop.DTO.ProductImage.ProductImageDto;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductImageClient;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageClientService {

    private final ProductImageClient productImageClient;

    public ProductImageDto.ListResponse getProductImages(Long productId) {
        try {
            ResponseEntity<ProductImageDto.ListResponse> response = productImageClient.getProductImages(productId);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching product images for productId: {}", productId, e);
            return createEmptyListResponse(productId);
        }
    }

    public ProductImageDto.Response getImageById(Long imageId) {
        try {
            ResponseEntity<ProductImageDto.Response> response = productImageClient.getImageById(imageId);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching image with id: {}", imageId, e);
            return null;
        }
    }

    public ProductImageDto.Response uploadImage(
            Long productId,
            MultipartFile imageFile,
            String imageType,
            String alt,
            Integer sortOrder) {
        try {
            ResponseEntity<ProductImageDto.Response> response =
                    productImageClient.uploadImage(productId, imageFile, imageType, alt, sortOrder);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error uploading image for productId: {}", productId, e);
            return null;
        }
    }

    public ProductImageDto.Response updateImage(
            Long imageId,
            MultipartFile imageFile,
            String imageType,
            String alt,
            Integer sortOrder) {
        try {
            ResponseEntity<ProductImageDto.Response> response =
                    productImageClient.updateImage(imageId, imageFile, imageType, alt, sortOrder);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error updating image with id: {}", imageId, e);
            return null;
        }
    }

    public boolean deleteImage(Long imageId) {
        try {
            ResponseEntity<Void> response = productImageClient.deleteImage(imageId);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error deleting image with id: {}", imageId, e);
            return false;
        }
    }

    public boolean updateImageOrder(Long productId, List<Long> imageIds) {
        try {
            ResponseEntity<Void> response = productImageClient.updateImageOrder(productId, imageIds);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error updating image order for productId: {}", productId, e);
            return false;
        }
    }

    public ProductImageDto.Response setMainImage(Long imageId) {
        try {
            ResponseEntity<ProductImageDto.Response> response = productImageClient.setMainImage(imageId);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error setting main image with id: {}", imageId, e);
            return null;
        }
    }

    private ProductImageDto.ListResponse createEmptyListResponse(Long productId) {
        return ProductImageDto.ListResponse.builder()
                .productId(productId)
                .images(Collections.emptyList())
                .build();
    }
}