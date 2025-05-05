package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.DTO.PageResponse;
import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.DTO.product.ProductDto;
import com.example.ui_rozetkaonlineshop.service.BrandService;
import com.example.ui_rozetkaonlineshop.service.CategoryService;
import com.example.ui_rozetkaonlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final BrandService brandService;

    @GetMapping("/{slug}")
    public String viewCategory(
            @PathVariable String slug,
            @RequestParam(required = false) List<Long> brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "popularity") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        log.info("Запрос на просмотр категории: {}", slug);

        try {
            // Получаем категорию по slug
            CategoryDto.CategoryDetailsDto category = categoryService.getCategoryBySlug(slug);

            // Получаем родительскую категорию (для хлебных крошек)
            CategoryDto.CategoryListDto parentCategory = null;
            if (category.getParentId() != null) {
                parentCategory = categoryService.getCategoryShortInfo(category.getParentId());
            }

            // Получаем подкатегории
            List<CategoryDto.CategoryListDto> subcategories = categoryService.getSubcategories(category.getId());

            // Получаем все бренды в данной категории
            List<BrandDto.BrandListDTO> brands = brandService.getBrandsByCategory(category.getId());

            // Настраиваем сортировку и фильтрацию
            PageResponse<ProductDto.ProductListDTO> products = productService.getFilteredProducts(
                    category.getId(), brand, minPrice, maxPrice, sort, page, size);

            // Добавляем все данные в модель
            model.addAttribute("category", category);
            model.addAttribute("parentCategory", parentCategory);
            model.addAttribute("subcategories", subcategories);
            model.addAttribute("brands", brands);
            model.addAttribute("products", products.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", products.getTotalPages());
            model.addAttribute("totalItems", products.getTotalElements());

            // Сохраняем текущие параметры для пагинации
            model.addAttribute("currentUrl", "/category/" + slug);
            model.addAttribute("selectedBrands", brand);
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);
            model.addAttribute("sortBy", sort);

            // Увеличиваем счетчик просмотров категории
            categoryService.incrementViewCount(category.getId());

            return "client/categories/view";

        } catch (Exception e) {
            log.error("Ошибка при отображении категории: {}", e.getMessage());
            return "error/404";
        }
    }



    /**
     * Отображает список всех корневых категорий для пользователей
     */
    @GetMapping
    public String listCategories(Model model) {
        log.info("Запрос на отображение списка категорий для пользователей");
        List<CategoryDto.CategoryListDto> rootCategories = categoryService.getRootCategories();
        model.addAttribute("categories", rootCategories);
        return "client/categories/list";
    }

//    /**
//     * Отображает детальную информацию о категории по её ID
//     * Включает список подкатегорий и другую информацию
//     */
//    @GetMapping("/{id}")
//    public String viewCategory(@PathVariable Long id, Model model) {
//        log.info("Запрос на просмотр категории с ID: {}", id);
//        try {
//            CategoryDto.CategoryDetailsDto category = categoryService.getCategoryById(id);
//            List<CategoryDto.CategoryListDto> subcategories = categoryService.getSubcategories(id);
//
//            model.addAttribute("category", category);
//            model.addAttribute("subcategories", subcategories);
//
//            // Здесь можно добавить информацию о продуктах в категории,
//            // если у вас есть соответствующий сервис
//            // model.addAttribute("products", productService.getProductsByCategory(id));
//
//            return "client/categories/view";
//        } catch (Exception e) {
//            log.error("Ошибка при загрузке категории", e);
//            model.addAttribute("errorMessage", "Категория не найдена");
//            return "redirect:/categories";
//        }
//    }

    /**
     * Отображает категорию по её slug (ЧПУ)
     */
    @GetMapping("/by-slug/{slug}")
    public String viewCategoryBySlug(@PathVariable String slug, Model model) {
        log.info("Запрос на просмотр категории по slug: {}", slug);
        try {
            // Предполагается, что у вас есть метод для получения категории по slug
            // Если такого метода нет, его нужно добавить в сервис и клиент
            CategoryDto.CategoryDetailsDto category = categoryService.getCategoryBySlug(slug);
            List<CategoryDto.CategoryListDto> subcategories = categoryService.getSubcategories(category.getId());

            model.addAttribute("category", category);
            model.addAttribute("subcategories", subcategories);

            return "client/categories/view";
        } catch (Exception e) {
            log.error("Ошибка при загрузке категории по slug", e);
            model.addAttribute("errorMessage", "Категория не найдена");
            return "redirect:/categories";
        }
    }

    /**
     * Отображает результаты поиска категорий
     */
    @GetMapping("/search")
    public String searchCategories(@RequestParam String query, Model model) {
        log.info("Запрос на поиск категорий по запросу: {}", query);
        List<CategoryDto.CategoryListDto> searchResults = categoryService.searchCategories(query);

        model.addAttribute("query", query);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("resultsCount", searchResults.size());

        return "client/categories/search-results";
    }

    /**
     * Отображает список популярных категорий
     */
    @GetMapping("/popular")
    public String listPopularCategories(Model model) {
        log.info("Запрос на отображение популярных категорий");
        List<CategoryDto.PopularCategoryDto> popularCategories = categoryService.getPopularCategories();
        model.addAttribute("categories", popularCategories);
        return "client/categories/popular";
    }

    /**
     * Отображает хлебные крошки для категории (для AJAX запросов)
     */
    @GetMapping("/{id}/breadcrumbs")
    @ResponseBody
    public List<CategoryDto.CategoryListDto> getCategoryBreadcrumbs(@PathVariable Long id) {
        log.info("Запрос на получение хлебных крошек для категории с ID: {}", id);

        // Предполагается, что у вас есть метод для получения цепочки категорий (хлебных крошек)
        // Если такого метода нет, его нужно добавить в сервис и клиент
        return categoryService.getCategoryBreadcrumbs(id);
    }

    /**
     * Возвращает полное дерево категорий (для AJAX запросов, например, для мобильного меню)
     */
    @GetMapping("/tree")
    @ResponseBody
    public List<CategoryDto.CategoryTreeDto> getCategoryTree() {
        log.info("Запрос на получение дерева категорий");

        // Предполагается, что у вас есть метод для получения дерева категорий
        // Если такого метода нет, его нужно добавить в сервис и клиент
        return categoryService.getCategoryTree();
    }
}
