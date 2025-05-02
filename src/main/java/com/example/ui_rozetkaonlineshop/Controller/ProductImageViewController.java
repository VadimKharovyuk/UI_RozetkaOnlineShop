package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.ProductImage.ProductImageDto;
import com.example.ui_rozetkaonlineshop.service.ProductImageClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductImageViewController {

    private final ProductImageClientService imageService;

    @GetMapping("/{productId}/images")
    public String getProductImages(@PathVariable Long productId, Model model) {
        ProductImageDto.ListResponse images = imageService.getProductImages(productId);
        model.addAttribute("productImages", images);
        model.addAttribute("productId", productId);

        return "product/gallery";
    }

    @GetMapping("/images/{imageId}")
    public String getImageDetails(@PathVariable Long imageId, Model model) {
        ProductImageDto.Response image = imageService.getImageById(imageId);
        if (image == null) {
            return "product/image-not-found";
        }

        model.addAttribute("image", image);
        // Получаем все изображения продукта для галереи
        ProductImageDto.ListResponse productImages =
                imageService.getProductImages(image.getProductId());
        model.addAttribute("productImages", productImages);

        return "product/image-detail";
    }
}