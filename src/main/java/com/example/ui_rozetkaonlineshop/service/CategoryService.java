package com.example.ui_rozetkaonlineshop.service;


import com.example.ui_rozetkaonlineshop.DTO.category.CategoryDto;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final ProductServiceClient categoryServiceClient;

    public CategoryDto.CategoryDetailsDto createCategory(CategoryDto.CategoryCreateDto categoryDto,
                                                         MultipartFile imageFile) {
        try {
            log.info("Отправка запроса на создание новой категории: {}", categoryDto.getName());
            ResponseEntity<CategoryDto.CategoryDetailsDto> response;

            if (imageFile != null && !imageFile.isEmpty()) {
                response = categoryServiceClient.createCategory(categoryDto, imageFile);
            } else {
                response = categoryServiceClient.createCategoryWithoutImage(categoryDto);
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при создании категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при создании категории: " + e.getMessage());
        }
    }

    public CategoryDto.CategoryDetailsDto updateCategory(Long id,
                                                         CategoryDto.CategoryCreateDto categoryDto,
                                                         MultipartFile image) {
        try {
            log.info("Отправка запроса на обновление категории с ID {}: {}", id, categoryDto.getName());
            ResponseEntity<CategoryDto.CategoryDetailsDto> response =
                    categoryServiceClient.updateCategory(id, categoryDto, image);
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении категории: " + e.getMessage());
        }
    }

    public CategoryDto.CategoryDetailsDto getCategoryById(Long id) {
        try {
            log.info("Отправка запроса на получение категории по ID: {}", id);
            ResponseEntity<CategoryDto.CategoryDetailsDto> response =
                    categoryServiceClient.getCategoryById(id);
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении категории: " + e.getMessage());
        }
    }


    public List<CategoryDto.CategoryListDto> getRootCategories() {
        try {
            log.info("Отправка запроса на получение корневых категорий");
            ResponseEntity<List<CategoryDto.CategoryListDto>> response =
                    categoryServiceClient.getRootCategories();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении корневых категорий: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении корневых категорий: " + e.getMessage());
        }
    }


    public List<CategoryDto.CategoryListDto> getSubcategories(Long parentId) {
        try {
            log.info("Отправка запроса на получение подкатегорий для категории с ID: {}", parentId);
            ResponseEntity<List<CategoryDto.CategoryListDto>> response =
                    categoryServiceClient.getSubcategories(parentId);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении подкатегорий: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении подкатегорий: " + e.getMessage());
        }
    }

    public void deleteCategory(Long id) {
        try {
            log.info("Отправка запроса на удаление категории с ID: {}", id);
            categoryServiceClient.deleteCategory(id);
        } catch (Exception e) {
            log.error("Ошибка при удалении категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при удалении категории: " + e.getMessage());
        }
    }


    public List<CategoryDto.CategoryListDto> searchCategories(String query) {
        try {
            log.info("Отправка запроса на поиск категорий по запросу: {}", query);
            ResponseEntity<List<CategoryDto.CategoryListDto>> response =
                    categoryServiceClient.searchCategories(query);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при поиске категорий: {}", e.getMessage());
            throw new RuntimeException("Ошибка при поиске категорий: " + e.getMessage());
        }
    }

    /**
     * Обновляет статус популярности категории
     */
    public void updatePopularStatus(Long id, boolean popular) {
        try {
            log.info("Отправка запроса на обновление статуса популярности для категории {}: {}", id, popular);
            categoryServiceClient.updatePopularStatus(id, popular);
        } catch (Exception e) {
            log.error("Ошибка при обновлении статуса популярности: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении статуса популярности: " + e.getMessage());
        }
    }


    public Long getCategoriesCount() {
        try {
            log.info("Отправка запроса на получение общего количества категорий");
            ResponseEntity<Long> response = categoryServiceClient.getCategoriesCount();
            return response.getBody() != null ? response.getBody() : 0L;
        } catch (Exception e) {
            log.error("Ошибка при получении количества категорий: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении количества категорий: " + e.getMessage());
        }
    }



    ///
    // И соответственно в CategoryService:

    public CategoryDto.CategoryDetailsDto getCategoryBySlug(String slug) {
        try {
            log.info("Отправка запроса на получение категории по slug: {}", slug);
            ResponseEntity<CategoryDto.CategoryDetailsDto> response =
                    categoryServiceClient.getCategoryBySlug(slug);
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении категории по slug: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении категории: " + e.getMessage());
        }
    }

    public List<CategoryDto.CategoryListDto> getPopularCategories() {
        try {
            log.info("Отправка запроса на получение популярных категорий");
            ResponseEntity<List<CategoryDto.CategoryListDto>> response =
                    categoryServiceClient.getPopularCategories();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении популярных категорий: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении популярных категорий: " + e.getMessage());
        }
    }

    public List<CategoryDto.CategoryListDto> getCategoryBreadcrumbs(Long id) {
        try {
            log.info("Отправка запроса на получение хлебных крошек для категории с ID: {}", id);
            ResponseEntity<List<CategoryDto.CategoryListDto>> response =
                    categoryServiceClient.getCategoryBreadcrumbs(id);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении хлебных крошек: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении хлебных крошек: " + e.getMessage());
        }
    }

    public List<CategoryDto.CategoryTreeDto> getCategoryTree() {
        try {
            log.info("Отправка запроса на получение дерева категорий");
            ResponseEntity<List<CategoryDto.CategoryTreeDto>> response =
                    categoryServiceClient.getCategoryTree();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении дерева категорий: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении дерева категорий: " + e.getMessage());
        }
    }

    /**
     * Получить все категории для выбора в форме создания продукта
     */
    public List<CategoryDto.CategoryListDto> getAllCategories() {
        ResponseEntity<List<CategoryDto.CategoryListDto>> response = categoryServiceClient.getRootCategories();
        if (response.getBody() != null) {
            return response.getBody();
        }
        return Collections.emptyList();
    }
}