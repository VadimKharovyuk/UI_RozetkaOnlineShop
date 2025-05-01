package com.example.ui_rozetkaonlineshop.AdminController;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.service.BrandService;
import com.example.ui_rozetkaonlineshop.service.CategoryService;
import com.example.ui_rozetkaonlineshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

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
    /**
     * Форма создания нового продукта
     */
    @GetMapping("/create")
    public String createProductForm(Model model) {
        // Добавляем пустой объект запроса для создания продукта
        model.addAttribute("productCreateRequest", new ProductDto.ProductCreateRequest());

        // Получаем список категорий и добавляем их в модель
        List<CategoryDto.CategoryListDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Получаем список брендов и добавляем их в модель
        List<BrandDto.BrandListDTO> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);

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
    /**
     /**
     * Форма редактирования продукта
     */
//    @GetMapping("/{id}/edit")
//    public String editProductForm(@PathVariable Long id, Model model) {
//        Optional<ProductDto> productOptional = productService.getProductById(id);
//
//        if (productOptional.isPresent()) {
//            ProductDto product = productOptional.get();
//
//            // Здесь мы преобразуем ProductDto в ProductDto.ProductDTO
//            ProductDto.ProductDTO productDTO = convertToProductDTO(product);
//
//            // Теперь можно использовать наш существующий метод
//            ProductDto.ProductUpdateRequest updateRequest = convertToProductDTO(productDTO);
//
//            model.addAttribute("product", productDTO);
//            model.addAttribute("updateRequest", updateRequest);
//
//            // Получаем и добавляем список категорий
//            List<CategoryDto.CategoryListDto> categories = categoryService.getAllCategories();
//            model.addAttribute("categories", categories);
//
//            // Получаем и добавляем список брендов
//            List<BrandDto.BrandListDTO> brands = brandService.getAllBrands();
//            model.addAttribute("brands", brands);
//
//            return "admin/products/edit";
//        } else {
//            // Обработка случая, когда продукт не найден
//            return "redirect:/admin/products?error=Product+not+found";
//        }
//    }

    /**
     * Преобразование ProductDto в ProductDto.ProductDTO
     */
    private ProductDto.ProductDTO convertToProductDTO(ProductDto.ProductDTO product) {
        // Предполагаем, что у ProductDto есть getters для всех полей
        return ProductDto.ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .slug(product.getSlug())
                .categories(product.getCategories())
                .images(product.getImages())
                .attributes(product.getAttributes())
                .brand(product.getBrand())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .weight(product.getWeight())
                .height(product.getHeight())
                .width(product.getWidth())
                .depth(product.getDepth())
                .status(product.getStatus())
                .featured(product.isFeatured())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .metaTitle(product.getMetaTitle())
                .metaDescription(product.getMetaDescription())
                .metaKeywords(product.getMetaKeywords())
                .mainImageUrl(product.getMainImageUrl())
                .build();
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