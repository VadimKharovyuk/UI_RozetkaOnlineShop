package com.example.ui_rozetkaonlineshop.AdminController;
import com.example.ui_rozetkaonlineshop.DTO.user.UserDto;
import com.example.ui_rozetkaonlineshop.service.AuthService;
import com.example.ui_rozetkaonlineshop.service.BrandService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для основных страниц административной панели.
 * Обрабатывает запросы к главной странице админки и другим общим разделам.
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService ;


    @GetMapping
    public String adminDashboard(Model model, HttpServletRequest request) {
        log.info("Запрос главной страницы админ-панели");

        // Проверка административного доступа
        String redirectUrl = authService.checkAdminAccess(request, model);
        if (redirectUrl != null) {
            return redirectUrl;
        }

        try {
            // Получаем статистику и основные показатели для дашборда
            Map<String, Object> stats = getAdminDashboardStats();
            model.addAttribute("stats", stats);

            return "admin/dashboard";
        } catch (Exception e) {
            log.error("Ошибка при загрузке дашборда: {}", e.getMessage(), e);
            model.addAttribute("error", "Произошла ошибка при загрузке информации. Пожалуйста, попробуйте позже.");
            return "admin/dashboard";
        }
    }



    /**
     * Страница системных настроек
     */
    @GetMapping("/settings")
    public String adminSettings(Model model) {
        log.info("Запрос страницы настроек админ-панели");
        return "admin/settings";
    }

    /**
     * Страница профиля администратора
     */
    @GetMapping("/profile")
    public String adminProfile(Model model) {
        log.info("Запрос страницы профиля администратора");
        return "admin/profile";
    }

    /**
     * Метод для сбора статистических данных для дашборда
     */
    private Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Получаем информацию о брендах
            stats.put("totalBrands", 0); // Временно заглушка
            stats.put("activeBrands", 0); // Временно заглушка

            // Заглушки для других показателей, которые будут реализованы позже
            stats.put("totalProducts", 0);
            stats.put("totalCategories", 0);
            stats.put("totalOrders", 0);
            stats.put("pendingOrders", 0);
            stats.put("totalCustomers", 0);
            stats.put("totalRevenue", 0);

            // Статистика за текущий месяц
            stats.put("monthlyRevenue", 0);
            stats.put("monthlyOrders", 0);

            return stats;
        } catch (Exception e) {
            log.error("Ошибка при получении статистики для дашборда: {}", e.getMessage());
            return stats;
        }
    }
}