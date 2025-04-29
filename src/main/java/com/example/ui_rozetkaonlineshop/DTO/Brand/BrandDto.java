package com.example.ui_rozetkaonlineshop.DTO.Brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class BrandDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandDTO {
        private Long id;
        private String name;
        private String description;
        private String bannerUrl;
        private String bannerImageId;
        private String slug;
        private String metaKeywords;
        private boolean active;
        private boolean premium;
        private String country;
        private Integer foundedYear;
        private Integer productCount;
        private Integer sortOrder;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandListDTO {
        private Long id;
        private String name;
        private String slug;
        private String bannerUrl;
        private boolean active;
        private boolean premium;
        private String country;
        private Integer productCount;
        private Integer sortOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandCreateRequest {
        @NotBlank(message = "Название бренда не может быть пустым")
        private String name;

        private String description;
        private MultipartFile banner; // Для загрузки баннера
        private String slug;
        private String metaKeywords;
        private boolean active = true;
        private boolean premium = false;
        private String country;
        private Integer foundedYear;
        private Integer sortOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandUpdateRequest {
        private String name;
        private String description;
        private MultipartFile banner; // Для загрузки баннера
        private String slug;
        private String metaKeywords;
        private Boolean active;
        private Boolean premium;
        private String country;
        private Integer foundedYear;
        private Integer sortOrder;
    }
}
