package com.example.ui_rozetkaonlineshop.FeignClient;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        name = "PRODUCT",
        contextId = "brandClient",
        configuration = FeignConfig.class
)
public interface BrandServiceClient {


    @PostMapping("/api/products/admin/brands")
    ResponseEntity<BrandDto.BrandDTO> createBrand(
            @Valid @RequestBody BrandDto.BrandCreateRequest request);

    @PostMapping(value = "/api/products/admin/brands/{id}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BrandDto.BrandDTO> uploadBannerImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file);


    @GetMapping("/api/products/admin/brands")
    ResponseEntity<List<BrandDto.BrandListDTO>> getAllAdminBrands(
            @RequestParam(required = false) Boolean active);


    @PutMapping("/api/products/admin/brands/{id}")
    ResponseEntity<BrandDto.BrandDTO> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandDto.BrandUpdateRequest request);

    @DeleteMapping("/api/products/admin/brands/{id}")
    ResponseEntity<Void> deleteBrand(@PathVariable Long id);


    @DeleteMapping("/api/products/admin/brands/{id}/banner")
    ResponseEntity<Void> deleteBannerImage(@PathVariable Long id);


    // Публичные эндпоинты для брендов

    @GetMapping("/api/products/public/brands")
    ResponseEntity<List<BrandDto.BrandListDTO>> getAllPublicBrands(
            @RequestParam(required = false) Boolean active);

    @GetMapping("/api/products/public/brands/paginated")
    ResponseEntity<PageResponse<BrandDto.BrandListDTO>> getPaginatedPublicBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection);

    @GetMapping("/api/products/public/brands/search")
    ResponseEntity<List<BrandDto.BrandListDTO>> searchPublicBrands(
            @RequestParam String query);

    @GetMapping("/api/products/public/brands/premium")
    ResponseEntity<List<BrandDto.BrandListDTO>> getPublicPremiumBrands();

    @GetMapping("/api/products/public/brands/{id}")
    ResponseEntity<BrandDto.BrandDTO> getPublicBrandById(@PathVariable Long id);

    @GetMapping("/api/products/public/brands/slug/{slug}")
    ResponseEntity<BrandDto.BrandDTO> getPublicBrandBySlug(@PathVariable String slug);

    @GetMapping("/api/products/public/brands/count")
    Long getPublicBrandsCount();

    @GetMapping("/api/products/brands/category/{categoryId}")
    ResponseEntity<List<BrandDto.BrandListDTO>> getBrandsByCategory(@PathVariable Long categoryId);

}