package com.example.ui_rozetkaonlineshop.DTO.ProductAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

public class ProductAttributeDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductAttributeDTO {
        private Long id;
        private Long productId;
        private String color;
        private String size;
        private String material;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductAttributeListDTO {
        private Long productId;
        private List<ProductAttributeDTO> attributes = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductAttributeCreateDTO {
        private Long productId;
        private String color;
        private String size;
        private String material;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductAttributeUpdateDTO {
        private Long id;
        private String color;
        private String size;
        private String material;
    }
}