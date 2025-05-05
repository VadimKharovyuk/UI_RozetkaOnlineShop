package com.example.ui_rozetkaonlineshop.service;

import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductServiceClient productClient;

    /**
     * Получить все продукты
     */
    public List<ProductDto.ProductListDTO> getAllProducts() {
        ResponseEntity<List<ProductDto.ProductListDTO>> response = productClient.getAllProducts();
        return response.getBody();
    }

    /**
     * Получить продукт по ID
     */
    public Optional<ProductDto> getProductById(Long id) {
        try {
            ResponseEntity<ProductDto> response = productClient.getProductById(id);
            return Optional.ofNullable(response.getBody());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    /**
     * Создать новый продукт
     */
    public ProductDto.ProductDTO createProduct(ProductDto.ProductCreateRequest request) {
        ResponseEntity<ProductDto.ProductDTO> response = productClient.createProduct(request);
        return response.getBody();
    }

    /**
     * Обновить существующий продукт
     */
    public ProductDto.ProductDTO updateProduct(Long id, ProductDto.ProductUpdateRequest request) {
        ResponseEntity<ProductDto.ProductDTO> response = productClient.updateProduct(id, request);
        return response.getBody();
    }

    /**
     * Поиск продуктов по запросу
     */
    public ProductDto.ProductSearchResponse searchProducts(String query, int page, int size) {
        ResponseEntity<ProductDto.ProductSearchResponse> response =
                productClient.searchProducts(query, page, size);
        return response.getBody();
    }

    /**
     * Добавить изображение продукта
     */
    public ProductDto.ProductDTO addProductImage(Long id, MultipartFile image, boolean main) {
        try {
            ResponseEntity<ProductDto.ProductDTO> response =
                    productClient.addProductImage(id, image, main);
            return response.getBody();
        } catch (FeignException e) {
            throw new RuntimeException("Ошибка при загрузке изображения: " + e.getMessage(), e);
        }
    }

    /**
     * Получить продукты с пагинацией
     */
    public PageResponse<ProductDto.ProductListDTO> getProductsPaginated(
            int page, int size, String sort, String direction) {
        ResponseEntity<PageResponse<ProductDto.ProductListDTO>> response =
                productClient.getProductsPaginated(page, size, sort, direction);
        return response.getBody();
    }

    /**
     * Получить продукты по категории
     */
    public List<ProductDto.ProductListDTO> getProductsByCategory(Long categoryId) {
        ResponseEntity<List<ProductDto.ProductListDTO>> response =
                productClient.getProductsByCategory(categoryId);
        return response.getBody();
    }

    /**
     * Получить продукт по slug
     */
    public Optional<ProductDto> getProductBySlug(String slug) {
        try {
            ResponseEntity<ProductDto> response = productClient.getProductBySlug(slug);
            return Optional.ofNullable(response.getBody());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    /**
     * Удалить продукт
     */
    public void deleteProduct(Long id) {
        productClient.deleteProduct(id);
    }

    /**
     * Получить продукты по бренду
     */
    public List<ProductDto.ProductListDTO> getProductsByBrand(Long brandId) {
        ResponseEntity<List<ProductDto.ProductListDTO>> response =
                productClient.getProductsByBrand(brandId);
        return response.getBody();
    }

    /**
     * Получить продукты по статусу
     */
    public List<ProductDto.ProductListDTO> getProductsByStatus(String status) {
        ResponseEntity<List<ProductDto.ProductListDTO>> response =
                productClient.getProductsByStatus(status);
        return response.getBody();
    }

    /**
     * Получить избранные продукты
     */
    public List<ProductDto.ProductListDTO> getFeaturedProducts() {
        ResponseEntity<List<ProductDto.ProductListDTO>> response =
                productClient.getFeaturedProducts();
        return response.getBody();
    }

    /**
     * Поиск продуктов по цене
     */
    public ProductDto.ProductSearchResponse searchProductsByPrice(
            BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        ResponseEntity<ProductDto.ProductSearchResponse> response =
                productClient.searchProductsByPrice(minPrice, maxPrice, page, size);
        return response.getBody();
    }

    /**
     * Увеличить счетчик просмотров продукта
     */
    public void incrementViewCount(Long id) {
        productClient.incrementViewCount(id);
    }

    /**
     * Получить общее количество продуктов
     */
    public Long getProductCount() {
        ResponseEntity<Long> response = productClient.getProductCount();
        return response.getBody();
    }

    /**
     * Получить количество продуктов по статусу
     */
    public Long getProductCountByStatus(String status) {
        ResponseEntity<Long> response = productClient.getProductCountByStatus(status);
        return response.getBody();
    }

    /**
     * Обновить количество товара на складе
     */
    public void updateStockQuantity(Long id, Integer quantity) {
        productClient.updateStockQuantity(id, quantity);
    }


    public PageResponse<ProductDto.ProductListDTO> getFilteredProducts(
            Long categoryId,
            List<Long> brandIds,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sort,
            int page,
            int size) {
        try {
            log.info("Отправка запроса на получение отфильтрованных продуктов для категории: {}", categoryId);

            ResponseEntity<PageResponse<ProductDto.ProductListDTO>> response =
                    productClient.getFilteredProducts(categoryId, brandIds, minPrice, maxPrice, sort, page, size);

            if (response.getBody() != null) {
                return response.getBody();
            } else {
                // Возвращаем пустую страницу, если ответ пустой
                return new PageResponse<>(
                        Collections.emptyList(),  // content
                        0,                       // pageNumber
                        0,                       // pageSize
                        0L,                      // totalElements (используем L для явного указания типа long)
                        0,                       // totalPages
                        true,                    // first
                        true,                    // last
                        true                     // empty
                );
            }
        } catch (Exception e) {
            log.error("Ошибка при получении отфильтрованных продуктов: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении отфильтрованных продуктов: " + e.getMessage());
        }
    }
}