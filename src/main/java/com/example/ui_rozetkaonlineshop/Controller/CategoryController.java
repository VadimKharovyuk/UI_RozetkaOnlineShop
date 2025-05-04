package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

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

    /**
     * Отображает детальную информацию о категории по её ID
     * Включает список подкатегорий и другую информацию
     */
    @GetMapping("/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        log.info("Запрос на просмотр категории с ID: {}", id);
        try {
            CategoryDto.CategoryDetailsDto category = categoryService.getCategoryById(id);
            List<CategoryDto.CategoryListDto> subcategories = categoryService.getSubcategories(id);

            model.addAttribute("category", category);
            model.addAttribute("subcategories", subcategories);

            // Здесь можно добавить информацию о продуктах в категории,
            // если у вас есть соответствующий сервис
            // model.addAttribute("products", productService.getProductsByCategory(id));

            return "client/categories/view";
        } catch (Exception e) {
            log.error("Ошибка при загрузке категории", e);
            model.addAttribute("errorMessage", "Категория не найдена");
            return "redirect:/categories";
        }
    }

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
