package com.example.ui_rozetkaonlineshop.FeignClient;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(
        name = "PRODUCT",
        configuration = FeignConfig.class
)
public interface ProductServiceClient {

    // Административные эндпоинты для брендов


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


    /// нужно реализовать

    @GetMapping("/api/categories/public/by-slug/{slug}")
    ResponseEntity<CategoryDto.CategoryDetailsDto> getCategoryBySlug(@PathVariable String slug);

    @GetMapping("/api/categories/public/popular")
    ResponseEntity<List<CategoryDto.CategoryListDto>> getPopularCategories();

    @GetMapping("/api/categories/public/{id}/breadcrumbs")
    ResponseEntity<List<CategoryDto.CategoryListDto>> getCategoryBreadcrumbs(@PathVariable Long id);

    @GetMapping("/api/categories/public/tree")
    ResponseEntity<List<CategoryDto.CategoryTreeDto>> getCategoryTree();


    /// продуктовый крнтролер


    /**
     * Обновить существующий продукт
     */
    @PutMapping("/api/products/{id}")
    ResponseEntity<ProductDto.ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto.ProductUpdateRequest request);
    /**
     * Получить все продукты
     */
    @GetMapping("/api/products")
    ResponseEntity<List<ProductDto.ProductListDTO>> getAllProducts();

    /**
     * Создать новый продукт
     */
    @PostMapping("/api/products")
    ResponseEntity<ProductDto.ProductDTO> createProduct(@Valid RequestBody ProductCreateRequest);

    /**
     * Обновить существующий продукт
     */
    @GetMapping("/api/products/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable Long id);


    /**
     * Получить продукт по slug
     */
    @GetMapping("/api/products/slug/{slug}")
    ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug);



    /**
     * Удалить продукт
     */
    @DeleteMapping("/api/products/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id);


    /**
     * Получить продукты с пагинацией
     */
    @PostMapping("/api/products/page")
    ResponseEntity<PageResponse<ProductDto.ProductListDTO>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    );

    /**
     * Получить продукты по бренду
     */
    @GetMapping("/api/products/brand/{brandId}")
    ResponseEntity<List<ProductDto.ProductListDTO>> getProductsByBrand(@PathVariable Long brandId);


    /**
     * Получить продукты по категории
     */
    @GetMapping("/api/products/category/{categoryId}")
    ResponseEntity<List<ProductDto.ProductListDTO>> getProductsByCategory(@PathVariable Long categoryId);
    /**
     * Получить продукты по статусу
     */
    @GetMapping("/api/products/status/{status}")
    ResponseEntity<List<ProductDto.ProductListDTO>> getProductsByStatus(
            @PathVariable String status);

    /**
     * Получить избранные продукты
     */
    @GetMapping("/api/products/featured")
    ResponseEntity<List<ProductDto.ProductListDTO>> getFeaturedProducts();


    /**
     * Поиск продуктов
     */
    @GetMapping("/api/products/search")
    ResponseEntity<ProductDto.ProductSearchResponse> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);


    /**
     * Поиск продуктов по цене
     */
    @GetMapping("/api/products/search/price")
    ResponseEntity<ProductDto.ProductSearchResponse> searchProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);


    /**
     * Загрузить изображение для продукта
     */
    @PostMapping(value = "/api/products/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ProductDto.ProductDTO> addProductImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image,
            @RequestParam(defaultValue = "false") boolean main);


    /**
     * Увеличить счетчик просмотров продукта
     */
    @PostMapping("/api/products/{id}/view")
    ResponseEntity<Void> incrementViewCount(@PathVariable Long id);

    /**
     * Получить общее количество продуктов
     */
    @GetMapping("/api/products/count")
    ResponseEntity<Long> getProductCount();

    /**
     * Получить количество продуктов по статусу
     */
    @GetMapping("/api/products/count/status/{status}")
    ResponseEntity<Long> getProductCountByStatus(@PathVariable String status);


    @PutMapping("/api/products/{id}/stock")
    ResponseEntity<Void> updateStockQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity);


}