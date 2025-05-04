package com.example.ui_rozetkaonlineshop.DTO.category;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CategoryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCreateDto {
        @NotBlank(message = "Название категории не может быть пустым")
        @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов")
        private String name;
        private String description;
        private String slug;
        private Long parentId;
        private Integer sortOrder;
        private boolean active = true;
        private String metaKeywords;
        private String metaTitle;


    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDetailsDto {
        private Long id;
        private String name;
        private String description;
        private String slug;
        private String imageUrl;
        private Long parentId;
        private String parentName;
        private boolean popular;
        private long viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // Возможно, список подкатегорий или другие детальные данные
        private List<CategoryListDto> subcategories;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryListDto {
        private Long id;
        private String name;
        private String slug;
        private String imageUrl;
        private boolean popular;
        private int subcategoriesCount;


    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryTreeDto {
        private Long id;
        private String name;
        private String slug;
        private List<CategoryTreeDto> children;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularCategoryDto {
        private Long id;
        private String name;
        private String imageUrl;
        private Integer productCount;
        private String slug;

        // Можно добавить показатель популярности
        private Integer popularityScore;

        // Дополнительные поля, которые могут быть полезны
        private BigDecimal minPrice; // Минимальная цена товара в категории
        private BigDecimal maxPrice; // Максимальная цена товара в категории
        private boolean hasDiscount; // Есть ли товары со скидкой
    }
}
