package com.example.ui_rozetkaonlineshop.FeignClient;

import com.example.ui_rozetkaonlineshop.DTO.ProductImage.ProductImageDto;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        name = "PRODUCT",
        contextId = "productImageClient",
        configuration = FeignConfig.class
)
public interface ProductImageClient {
    @GetMapping("/api/v1/product-images/product/{productId}")
    ResponseEntity<ProductImageDto.ListResponse> getProductImages(@PathVariable Long productId);

    @GetMapping("/api/v1/product-images/{imageId}")
    ResponseEntity<ProductImageDto.Response> getImageById(@PathVariable Long imageId);

    @PostMapping(value = "/api/v1/product-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ProductImageDto.Response> uploadImage(
            @RequestPart("productId") Long productId,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart(value = "imageType", required = false) String imageType,
            @RequestPart(value = "alt", required = false) String alt,
            @RequestPart(value = "sortOrder", required = false) Integer sortOrder);

    @PutMapping(value = "/api/v1/product-images/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ProductImageDto.Response> updateImage(
            @PathVariable Long imageId,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "imageType", required = false) String imageType,
            @RequestPart(value = "alt", required = false) String alt,
            @RequestPart(value = "sortOrder", required = false) Integer sortOrder);

    @DeleteMapping("/api/v1/product-images/{imageId}")
    ResponseEntity<Void> deleteImage(@PathVariable Long imageId);

    @PutMapping("/api/v1/product-images/order/{productId}")
    ResponseEntity<Void> updateImageOrder(
            @PathVariable Long productId,
            @RequestBody List<Long> imageIds);

    @PutMapping("/api/v1/product-images/main/{imageId}")
    ResponseEntity<ProductImageDto.Response> setMainImage(@PathVariable Long imageId);
}