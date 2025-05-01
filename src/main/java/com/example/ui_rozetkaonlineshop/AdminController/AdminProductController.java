package com.example.ui_rozetkaonlineshop.AdminController;

import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /**
     * Список продуктов в админке
     */
    @GetMapping
    public String productsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        PageResponse<ProductDto.ProductListDTO> productsPage =
                productService.getProductsPaginated(page, size, sort, direction);

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("currentPage", page);

        return "admin/products/list";
    }

    /**
     * Форма создания нового продукта
     */
    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("productCreateRequest", new ProductDto.ProductCreateRequest());
        return "admin/products/create";
    }

    /**
     * Обработка создания продукта
     */
    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute("productCreateRequest") ProductDto.ProductCreateRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/products/create";
        }

        ProductDto.ProductDTO createdProduct = productService.createProduct(request);
        redirectAttributes.addFlashAttribute("successMessage", "Продукт успешно создан!");

        return "redirect:/admin/products";
    }

    /**
     * Форма редактирования продукта
     */
    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> {
            // Здесь нужно создать ProductUpdateRequest из полученного ProductDTO
            ProductDto.ProductUpdateRequest updateRequest = new ProductDto.ProductUpdateRequest();
            // Заполнение полей updateRequest из product

            model.addAttribute("product", product);
            model.addAttribute("updateRequest", updateRequest);
        });

        return "admin/products/edit";
    }

    /**
     * Обработка обновления продукта
     */
    @PostMapping("/{id}/edit")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("updateRequest") ProductDto.ProductUpdateRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/products/edit";
        }

        ProductDto.ProductDTO updatedProduct = productService.updateProduct(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Продукт успешно обновлен!");

        return "redirect:/admin/products";
    }

    /**
     * Загрузка изображения продукта
     */
    @PostMapping("/{id}/images")
    public String uploadProductImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "main", defaultValue = "false") boolean main,
            RedirectAttributes redirectAttributes) {

        try {
            productService.addProductImage(id, image, main);
            redirectAttributes.addFlashAttribute("successMessage", "Изображение успешно загружено!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка загрузки изображения: " + e.getMessage());
        }

        return "redirect:/admin/products/" + id + "/edit";
    }

    /**
     * Удаление продукта
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Продукт успешно удален!");

        return "redirect:/admin/products";
    }

    /**
     * Обновление количества товара на складе
     */
    @PostMapping("/{id}/stock")
    public String updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {

        productService.updateStockQuantity(id, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Количество товара обновлено!");

        return "redirect:/admin/products/" + id + "/edit";
    }

    /**
     * Статистика продуктов
     */
    @GetMapping("/statistics")
    public String productStatistics(Model model) {
        Long totalProducts = productService.getProductCount();
        Long activeProducts = productService.getProductCountByStatus("ACTIVE");
        Long inactiveProducts = productService.getProductCountByStatus("INACTIVE");
        Long outOfStockProducts = productService.getProductCountByStatus("OUT_OF_STOCK");

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("inactiveProducts", inactiveProducts);
        model.addAttribute("outOfStockProducts", outOfStockProducts);

        return "admin/products/statistics";
    }

    /**
     * Поиск продуктов в админке
     */
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        ProductDto.ProductSearchResponse searchResults =
                productService.searchProducts(query, page, size);

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("query", query);

        return "admin/products/search-results";
    }
}