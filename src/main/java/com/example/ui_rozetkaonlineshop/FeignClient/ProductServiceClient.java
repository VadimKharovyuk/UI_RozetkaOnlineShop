package com.example.ui_rozetkaonlineshop.FeignClient;
import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
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

    @PostMapping("/api/products")
    ResponseEntity<ProductDto.ProductDTO> createProduct(@Valid @RequestBody ProductDto.ProductCreateRequest request);

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
    @GetMapping("/api/products/page")
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

    @GetMapping("/api/products/filter")
     ResponseEntity<PageResponse<ProductDto.ProductListDTO>> getFilteredProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> brandIds,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "popularity") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size);
}