package com.example.ui_rozetkaonlineshop.DTO.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductImageDTO {
        private Long id;
        private Long productId;
        private String imageType;
        private String imageUrl;
        private String imageId;
        private String alt;
        private Integer sortOrder;
        private LocalDateTime uploadedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductImageListDTO {
        private Long productId;
        private List<ProductImageDTO> images = new ArrayList<>();
    }

    // Для клиентского кода обычно не нужны классы для создания и обновления,
    // так как они используются только в запросах, которые будут формироваться
    // с использованием MultipartFile напрямую
    // Но для полноты реализации добавим их сюда

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductImageCreateRequest {
        private Long productId;
        private String imageType;
        private String alt;
        private Integer sortOrder;
        // MultipartFile не нужен здесь, так как он будет передаваться отдельно в запросе
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductImageUpdateRequest {
        private String imageType;
        private String imageId;
        private String alt;
        private Integer sortOrder;

    }
}