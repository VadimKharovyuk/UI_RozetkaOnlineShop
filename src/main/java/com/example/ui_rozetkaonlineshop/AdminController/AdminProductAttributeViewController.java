package com.example.ui_rozetkaonlineshop.AdminController;

import com.example.ui_rozetkaonlineshop.DTO.ProductAttribute.ProductAttributeClientDTO;
import com.example.ui_rozetkaonlineshop.service.ProductAttributeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/attributes")
@RequiredArgsConstructor
@Slf4j
public class AdminProductAttributeViewController {

    private final ProductAttributeClientService attributeService;

    @GetMapping("/{attributeId}/edit")
    public String editAttributeForm(@PathVariable Long attributeId, Model model) {
        ProductAttributeClientDTO.Response attribute = attributeService.getAttributeById(attributeId);
        if (attribute == null) {
            return "admin/attribute/not-found";
        }

        model.addAttribute("attribute", attribute);
        return "admin/attribute/attribute-edit";
    }

    @GetMapping("/products/{productId}/new")
    public String newAttributeForm(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "admin/attribute/attribute-new";
    }

    @PostMapping("/save")
    public String saveAttribute(@ModelAttribute ProductAttributeClientDTO.CreateRequest request) {
        attributeService.createAttribute(request);
        return "redirect:/admin/products/" + request.getProductId();
    }

    @PostMapping("/{attributeId}/update")
    public String updateAttribute(
            @PathVariable Long attributeId,
            @ModelAttribute ProductAttributeClientDTO.UpdateRequest request) {

        ProductAttributeClientDTO.Response updated = attributeService.updateAttribute(attributeId, request);
        if (updated == null) {
            return "admin/attribute/not-found";
        }

        return "redirect:/admin/products/" + updated.getProductId();
    }

    @GetMapping("/{attributeId}/delete")
    public String deleteAttribute(@PathVariable Long attributeId, @RequestParam Long productId) {
        boolean deleted = attributeService.deleteAttribute(attributeId);
        if (!deleted) {
            return "admin/attribute/not-found";
        }

        return "redirect:/admin/products/" + productId;
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String material,
            Model model) {

        // Комбинированный поиск по нескольким параметрам
        // В реальном приложении здесь была бы более сложная логика

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

        return "admin/attribute/attribute-search-results";
    }

    @GetMapping("/product/{productId}")
    public String getProductAttributes(@PathVariable Long productId, Model model) {
        ProductAttributeClientDTO.ListResponse attributes = attributeService.getProductAttributes(productId);
        model.addAttribute("productAttributes", attributes);
        model.addAttribute("productId", productId);

        return "admin/attribute/product-attributes";
    }
}