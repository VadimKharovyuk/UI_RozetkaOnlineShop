package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        List<CategoryDto.PopularCategoryDto> popularCategoryDtoList = categoryService.getPopularCategories();
        log.info("Количество популярных категорий на главной странице: " + popularCategoryDtoList.size());
        model.addAttribute("PopularCategories", popularCategoryDtoList);
        return "home";
    }
}