package com.example.ui_rozetkaonlineshop.AdminController;
import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public String listCategories(Model model) {
        log.info("Запрос на отображение списка категорий в админ-панели");
        List<CategoryDto.CategoryListDto> rootCategories = categoryService.getRootCategories();
        model.addAttribute("categories", rootCategories);
        return "admin/categories/list";

    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        log.info("Запрос на отображение формы создания категории");
        model.addAttribute("categoryDto", new CategoryDto.CategoryCreateDto());
        model.addAttribute("parentCategories", categoryService.getRootCategories());
        return "admin/categories/create";
    }

    @PostMapping("/create")
    public String createCategory(
            @Valid @ModelAttribute("categoryDto") CategoryDto.CategoryCreateDto categoryDto,
            BindingResult bindingResult,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Запрос на создание новой категории: {}", categoryDto.getName());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибка валидации при создании категории: {}", bindingResult.getAllErrors());
            model.addAttribute("parentCategories", categoryService.getRootCategories());
            return "admin/categories/create";
        }

        try {
            CategoryDto.CategoryDetailsDto createdCategory = categoryService.createCategory(categoryDto, imageFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Категория '" + createdCategory.getName() + "' успешно создана");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.error("Ошибка при создании категории", e);
            model.addAttribute("errorMessage", "Ошибка при создании категории: " + e.getMessage());
            model.addAttribute("parentCategories", categoryService.getRootCategories());
            return "admin/categories/create";
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Запрос на отображение формы редактирования категории с ID: {}", id);
        try {
            CategoryDto.CategoryDetailsDto category = categoryService.getCategoryById(id);

            // Создаем DTO для редактирования на основе полученных данных
            CategoryDto.CategoryCreateDto editDto = new CategoryDto.CategoryCreateDto();
            editDto.setName(category.getName());
            editDto.setDescription(category.getDescription());
            editDto.setSlug(category.getSlug());
            editDto.setParentId(category.getParentId());
            // Другие поля...

            model.addAttribute("categoryDto", editDto);
            model.addAttribute("categoryId", id);
            model.addAttribute("parentCategories", categoryService.getRootCategories());
            model.addAttribute("currentImageUrl", category.getImageUrl());

            return "admin/categories/edit";
        } catch (Exception e) {
            log.error("Ошибка при загрузке категории для редактирования", e);
            model.addAttribute("errorMessage", "Категория не найдена");
            return "redirect:/admin/categories";
        }
    }

    /**
     * Обрабатывает отправку формы для обновления существующей категории
     */
    @PostMapping("/edit/{id}")
    public String updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute("categoryDto") CategoryDto.CategoryCreateDto categoryDto,
            BindingResult bindingResult,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Запрос на обновление категории с ID {}: {}", id, categoryDto.getName());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибка валидации при обновлении категории: {}", bindingResult.getAllErrors());
            model.addAttribute("categoryId", id);
            model.addAttribute("parentCategories", categoryService.getRootCategories());
            return "admin/categories/edit";
        }

        try {
            CategoryDto.CategoryDetailsDto updatedCategory = categoryService.updateCategory(id, categoryDto, imageFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Категория '" + updatedCategory.getName() + "' успешно обновлена");
            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.error("Ошибка при обновлении категории", e);
            model.addAttribute("errorMessage", "Ошибка при обновлении категории: " + e.getMessage());
            model.addAttribute("categoryId", id);
            model.addAttribute("parentCategories", categoryService.getRootCategories());
            return "admin/categories/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        log.info("Запрос на удаление категории с ID: {}", id);

        try {
            // Получаем имя категории перед удалением для информационного сообщения
            CategoryDto.CategoryDetailsDto category = categoryService.getCategoryById(id);
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Категория '" + category.getName() + "' успешно удалена");
        } catch (Exception e) {
            log.error("Ошибка при удалении категории", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка при удалении категории: " + e.getMessage());
        }

        return "redirect:/admin/categories";
    }

    /**
     * Обрабатывает запрос на обновление статуса популярности категории
     */
    @PostMapping("/{id}/popular")
    public String updatePopularStatus(
            @PathVariable Long id,
            @RequestParam boolean popular,
            RedirectAttributes redirectAttributes) {

        log.info("Запрос на изменение статуса популярности категории с ID {}: {}", id, popular);

        try {
            categoryService.updatePopularStatus(id, popular);
            String status = popular ? "популярной" : "обычной";
            redirectAttributes.addFlashAttribute("successMessage",
                    "Категория успешно отмечена как " + status);
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса популярности", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка при обновлении статуса популярности: " + e.getMessage());
        }

        return "redirect:/admin/categories";
    }

    /**
     * Отображает информацию о подкатегориях
     */
    @GetMapping("/{id}/subcategories")
    public String viewSubcategories(@PathVariable Long id, Model model) {
        log.info("Запрос на просмотр подкатегорий для категории с ID: {}", id);

        try {
            CategoryDto.CategoryDetailsDto parentCategory = categoryService.getCategoryById(id);
            List<CategoryDto.CategoryListDto> subcategories = categoryService.getSubcategories(id);

            model.addAttribute("parentCategory", parentCategory);
            model.addAttribute("subcategories", subcategories);
            return "admin/categories/subcategories";
        } catch (Exception e) {
            log.error("Ошибка при загрузке подкатегорий", e);
            model.addAttribute("errorMessage", "Ошибка при загрузке подкатегорий: " + e.getMessage());
            return "redirect:/admin/categories";
        }
    }
}
