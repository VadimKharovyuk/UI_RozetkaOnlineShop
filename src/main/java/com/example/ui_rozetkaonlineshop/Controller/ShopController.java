package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ProductService productService;

    /**
     * Домашняя страница магазина
     */
    @GetMapping
    public String homePage(Model model) {
        List<ProductDto.ProductListDTO> featuredProducts = productService.getFeaturedProducts();
        model.addAttribute("featuredProducts", featuredProducts);
        return "shop/home";
    }

    /**
     * Отображение каталога продуктов
     */
    @GetMapping("/catalog")
    public String catalogPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        PageResponse<ProductDto.ProductListDTO> productsPage =
                productService.getProductsPaginated(page, size, sort, direction);

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        return "shop/catalog";
    }

    /**
     * Просмотр детальной страницы продукта
     */
    @GetMapping("/product/{id}")
    public String productDetailPage(@PathVariable Long id, Model model) {
        productService.incrementViewCount(id);

        productService.getProductById(id).ifPresent(product -> {
            model.addAttribute("product", product);
        });

        return "shop/product-detail";
    }

    /**
     * Просмотр продуктов по категории
     */
    @GetMapping("/category/{categoryId}")
    public String categoryProductsPage(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        List<ProductDto.ProductListDTO> products = productService.getProductsByCategory(categoryId);
        model.addAttribute("products", products);
        model.addAttribute("categoryId", categoryId);

        return "shop/category";
    }

    /**
     * Поиск продуктов
     */
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        ProductDto.ProductSearchResponse searchResults =
                productService.searchProducts(query, page, size);

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);

        return "shop/search-results";
    }

    /**
     * Поиск по ценовому диапазону
     */
    @GetMapping("/price-filter")
    public String filterByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        ProductDto.ProductSearchResponse searchResults =
                productService.searchProductsByPrice(minPrice, maxPrice, page, size);

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "shop/price-filter";
    }

    /**
     * Просмотр продуктов бренда
     */
    @GetMapping("/brand/{brandId}")
    public String brandProductsPage(@PathVariable Long brandId, Model model) {
        List<ProductDto.ProductListDTO> products = productService.getProductsByBrand(brandId);
        model.addAttribute("products", products);
        model.addAttribute("brandId", brandId);

        return "shop/brand";
    }
}