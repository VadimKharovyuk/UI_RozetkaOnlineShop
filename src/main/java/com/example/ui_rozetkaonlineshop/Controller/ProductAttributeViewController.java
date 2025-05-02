package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.ProductAttribute.ProductAttributeClientDTO;
import com.example.ui_rozetkaonlineshop.service.ProductAttributeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductAttributeViewController {

    private final ProductAttributeClientService attributeService;

    @GetMapping("/catalog")
    public String getProductCatalog(Model model) {
        // Здесь будет логика получения каталога продуктов
        // и добавления соответствующих данных в модель
        return "product/catalog";
    }

    @GetMapping("/{productId}")
    public String getProductDetails(@PathVariable Long productId, Model model) {
        // Получаем атрибуты продукта через клиентский сервис
        ProductAttributeClientDTO.ListResponse attributes = attributeService.getProductAttributes(productId);
        model.addAttribute("productAttributes", attributes);
        // Другие данные продукта (будут получены через другие сервисы)

        return "product/details";
    }

    @GetMapping("/filter/color/{color}")
    public String getProductsByColor(@PathVariable String color, Model model) {
        List<ProductAttributeClientDTO.Response> filteredAttributes = attributeService.findAttributesByColor(color);
        model.addAttribute("filteredAttributes", filteredAttributes);
        model.addAttribute("filterType", "color");
        model.addAttribute("filterValue", color);

        return "product/filtered-list";
    }

    @GetMapping("/filter/size/{size}")
    public String getProductsBySize(@PathVariable String size, Model model) {
        List<ProductAttributeClientDTO.Response> filteredAttributes = attributeService.findAttributesBySize(size);
        model.addAttribute("filteredAttributes", filteredAttributes);
        model.addAttribute("filterType", "size");
        model.addAttribute("filterValue", size);

        return "product/filtered-list";
    }

    @GetMapping("/filter/material/{material}")
    public String getProductsByMaterial(@PathVariable String material, Model model) {
        List<ProductAttributeClientDTO.Response> filteredAttributes = attributeService.findAttributesByMaterial(material);
        model.addAttribute("filteredAttributes", filteredAttributes);
        model.addAttribute("filterType", "material");
        model.addAttribute("filterValue", material);

        return "product/filtered-list";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String material,
            Model model) {

        if (color != null && !color.isEmpty()) {
            model.addAttribute("searchResults", attributeService.findAttributesByColor(color));
            model.addAttribute("filterType", "color");
            model.addAttribute("filterValue", color);
        } else if (size != null && !size.isEmpty()) {
            model.addAttribute("searchResults", attributeService.findAttributesBySize(size));
            model.addAttribute("filterType", "size");
            model.addAttribute("filterValue", size);
        } else if (material != null && !material.isEmpty()) {
            model.addAttribute("searchResults", attributeService.findAttributesByMaterial(material));
            model.addAttribute("filterType", "material");
            model.addAttribute("filterValue", material);
        }

        return "product/search-results";
    }
}