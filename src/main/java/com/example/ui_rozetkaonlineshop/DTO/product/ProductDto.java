package com.example.ui_rozetkaonlineshop.DTO.product;
import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.DTO.ProductAttribute.ProductAttributeDto;
import com.example.ui_rozetkaonlineshop.DTO.ProductImage.ProductImageDto;
import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCreateRequest {
        private String name;
        private String description;
        private String sku;
        private Set<Long> categoryIds = new HashSet<>();
        private Long brandId;
        private BigDecimal price;
        private Integer stockQuantity;
        private Double weight;
        private Double height;
        private Double width;
        private Double depth;
        private String status;
        private boolean featured;
        private String metaTitle;
        private String metaDescription;
        private String metaKeywords;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductUpdateRequest {
        private String name;
        private String description;
        private String sku;
        private Set<Long> categoryIds;
        private Long brandId;
        private BigDecimal price;
        private Integer stockQuantity;
        private Double weight;
        private Double height;
        private Double width;
        private Double depth;
        private String status;
        private boolean featured;
        private String metaTitle;
        private String metaDescription;
        private String metaKeywords;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDTO {
        private Long id;
        private String name;
        private String description;
        private String sku;
        private String slug;
        private Set<CategoryDto.CategoryListDto> categories = new HashSet<>();
        private List<ProductImageDto.ProductImageDTO> images = new ArrayList<>();
        private List<ProductAttributeDto.ProductAttributeDTO> attributes = new ArrayList<>();
        private BrandDto.BrandDTO brand;
        private BigDecimal price;
        private Integer stockQuantity;
        private Double weight;
        private Double height;
        private Double width;
        private Double depth;
        private String status;
        private boolean featured;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String metaTitle;
        private String metaDescription;
        private String metaKeywords;
        private String mainImageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductListDTO {
        private Long id;
        private String name;
        private String slug;
        private BigDecimal price;
        private String status;
        private boolean featured;
        private String mainImageUrl;
        private Long brandId;
        private String brandName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSearchResponse {
        private List<ProductListDTO> products;
        private long total;
        private int page;
        private int size;
    }
}