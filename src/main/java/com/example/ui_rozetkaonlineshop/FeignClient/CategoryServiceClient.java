package com.example.ui_rozetkaonlineshop.FeignClient;

import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        name = "PRODUCT",
        contextId = "categoryClient",
        configuration = FeignConfig.class
)
public interface CategoryServiceClient {

    /// Кстегории контрллер
    @PostMapping(value = "/api/categories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CategoryDto.CategoryDetailsDto> createCategory(
            @SpringQueryMap @Valid CategoryDto.CategoryCreateDto categoryDto,
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile);

    @PostMapping(value = "/api/categories", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDto.CategoryDetailsDto> createCategoryWithoutImage(
            @RequestBody @Valid CategoryDto.CategoryCreateDto categoryDto);


    @PutMapping(value = "/api/categories/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CategoryDto.CategoryDetailsDto> updateCategory(
            @PathVariable Long id,
            @RequestPart("data") @Valid CategoryDto.CategoryCreateDto categoryDto,
            @RequestPart(name = "image", required = false) MultipartFile image);

    @GetMapping("/api/categories/{id}")
    ResponseEntity<CategoryDto.CategoryDetailsDto> getCategoryById(@PathVariable Long id);

    @GetMapping("/api/categories")
    ResponseEntity<List<CategoryDto.CategoryListDto>> getRootCategories();

    @GetMapping("/api/categories/{id}/subcategories")
    ResponseEntity<List<CategoryDto.CategoryListDto>> getSubcategories(@PathVariable Long id);

    @DeleteMapping("/api/categories/{id}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long id);

    @GetMapping("/api/categories/search")
    ResponseEntity<List<CategoryDto.CategoryListDto>> searchCategories(@RequestParam String query);

    @PatchMapping("/api/categories/{id}/popular")
    ResponseEntity<Void> updatePopularStatus(
            @PathVariable Long id,
            @RequestParam boolean popular);

    @GetMapping("/api/categories/count")
    ResponseEntity<Long> getCategoriesCount();

    ///
    ///
    @GetMapping("/api/categories/public/popular")
    ResponseEntity<List<CategoryDto.PopularCategoryDto>> getPopularCategories();

    /// нужно реализовать

    @GetMapping("/api/categories/public/by-slug/{slug}")
    ResponseEntity<CategoryDto.CategoryDetailsDto> getCategoryBySlug(@PathVariable String slug);

    @GetMapping("/api/categories/public/{id}/breadcrumbs")
    ResponseEntity<List<CategoryDto.CategoryListDto>> getCategoryBreadcrumbs(@PathVariable Long id);

    @GetMapping("/api/categories/public/tree")
    ResponseEntity<List<CategoryDto.CategoryTreeDto>> getCategoryTree();

}