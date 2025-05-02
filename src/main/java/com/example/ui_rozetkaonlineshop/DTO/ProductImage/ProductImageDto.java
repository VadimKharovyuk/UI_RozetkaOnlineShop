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
    public static class Response {
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
    public static class ListResponse {
        private Long productId;
        private List<Response> images = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private Long productId;
        private String imageType;
        private String alt;
        private Integer sortOrder;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private Long id;
        private String imageType;
        private String alt;
        private Integer sortOrder;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderUpdateRequest {
        private Long productId;
        private List<Long> imageIds;
    }
}