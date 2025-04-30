package com.example.ui_rozetkaonlineshop.Controller;


import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;


import com.example.ui_rozetkaonlineshop.DTO.Brand.PageResponse;
import com.example.ui_rozetkaonlineshop.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandPublicController {

    private final BrandService brandService;


    // Публичная страница со списком брендов
    @GetMapping
    public String getAllBrands(Model model) {
        List<BrandDto.BrandListDTO> brands = brandService.getAllPublicBrands(true);
        model.addAttribute("brands", brands);
        return "client/brands/list";
    }

    // Публичная страница с постраничным списком брендов
    @GetMapping("/paginated")
    public String getPaginatedBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Model model) {

        PageResponse<BrandDto.BrandListDTO> brandsPage =
                brandService.getPaginatedPublicBrands(page, size, sortBy, sortDirection);

        model.addAttribute("brandsPage", brandsPage);
        return "client/brands/paginated";
    }

    // Публичная страница с премиальными брендами
    @GetMapping("/premium")
    public String getPremiumBrands(Model model) {
        List<BrandDto.BrandListDTO> premiumBrands = brandService.getPublicPremiumBrands();
        model.addAttribute("brands", premiumBrands);
        model.addAttribute("isPremium", true);
        return "client/brands/list";
    }

    // Публичная страница поиска брендов
    @GetMapping("/search")
    public String searchBrands(@RequestParam String query, Model model) {
        List<BrandDto.BrandListDTO> brands = brandService.searchPublicBrands(query);
        model.addAttribute("brands", brands);
        model.addAttribute("searchQuery", query);
        return "client/brands/list";
    }

    // Публичная страница с детальной информацией о бренде по ID
    @GetMapping("/{id}")
    public String getBrandById(@PathVariable Long id, Model model) {
        Optional<BrandDto.BrandDTO> brandOpt = brandService.getPublicBrandById(id);

        if (brandOpt.isEmpty()) {
            return "redirect:/brands?error=Brand+not+found";
        }

        model.addAttribute("brand", brandOpt.get());
        return "client/brands/detail";
    }

    // Публичная страница с детальной информацией о бренде по slug
    @GetMapping("/slug/{slug}")
    public String getBrandBySlug(@PathVariable String slug, Model model) {
        Optional<BrandDto.BrandDTO> brandOpt = brandService.getPublicBrandBySlug(slug);

        if (brandOpt.isEmpty()) {
            return "redirect:/brands?error=Brand+not+found";
        }

        model.addAttribute("brand", brandOpt.get());
        return "client/brands/detail";
    }

    // Публичные API эндпоинты для AJAX запросов

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<BrandDto.BrandListDTO>> getApiPublicBrands(
            @RequestParam(required = false) Boolean active) {
        List<BrandDto.BrandListDTO> brands = brandService.getAllPublicBrands(active != null ? active : true);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<BrandDto.BrandListDTO>> apiSearchBrands(
            @RequestParam String query) {
        List<BrandDto.BrandListDTO> brands = brandService.searchPublicBrands(query);
        return ResponseEntity.ok(brands);
    }
}