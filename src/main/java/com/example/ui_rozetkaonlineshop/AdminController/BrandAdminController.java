package com.example.ui_rozetkaonlineshop.AdminController;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/brand")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class BrandAdminController {
    private final BrandService brandService;

    // Административные страницы

    @GetMapping()
    public String getAdminBrandList(Model model) {
        List<BrandDto.BrandListDTO> brands = brandService.getAllAdminBrands(null);
        model.addAttribute("brands", brands);
        return "admin/brand/list";
    }



    @GetMapping("/create")
    public String showCreateBrandForm(Model model) {
        model.addAttribute("brandForm", new BrandDto.BrandCreateRequest());
        return "admin/brand/create";
    }

    @PostMapping("/create")
    public String createBrand(
            @Valid @ModelAttribute("brandForm") BrandDto.BrandCreateRequest request,
            @RequestParam(value = "banner", required = false) MultipartFile banner,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/brand/create";
        }

        try {
            // Сначала создать бренд
            BrandDto.BrandDTO createdBrand = brandService.createBrand(request);

            // Затем загрузить баннер, если он есть
            if (banner != null && !banner.isEmpty()) {
                brandService.uploadBannerImage(createdBrand.getId(), banner);
            }

            redirectAttributes.addFlashAttribute("success", "Бренд успешно создан");
            return "redirect:/admin/brand";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании бренда: " + e.getMessage());
            return "redirect:/admin/brand/create";
        }
    }
    @PostMapping("/{id}/banner")
    public String uploadBannerImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Пожалуйста, выберите файл для загрузки");
                return "redirect:/brands/admin/edit/" + id;
            }

            brandService.uploadBannerImage(id, file);
            redirectAttributes.addFlashAttribute("success", "Баннер успешно загружен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке баннера: " + e.getMessage());
        }

        return "redirect:/brands/admin/edit/" + id;
    }

    @GetMapping("/edit/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public String showEditBrandForm(@PathVariable Long id, Model model) {
        Optional<BrandDto.BrandDTO> brandOpt = brandService.getPublicBrandById(id);

        if (brandOpt.isEmpty()) {
            return "redirect:/brands/admin?error=Brand+not+found";
        }

        BrandDto.BrandDTO brand = brandOpt.get();
        BrandDto.BrandUpdateRequest updateForm = BrandDto.BrandUpdateRequest.builder()
                .name(brand.getName())
                .description(brand.getDescription())
                .slug(brand.getSlug())
                .metaKeywords(brand.getMetaKeywords())
                .active(brand.isActive())
                .premium(brand.isPremium())
                .country(brand.getCountry())
                .foundedYear(brand.getFoundedYear())
                .sortOrder(brand.getSortOrder())
                .build();

        model.addAttribute("brandId", id);
        model.addAttribute("brandForm", updateForm);
        model.addAttribute("currentBannerUrl", brand.getBannerUrl());

        return "admin/brand/edit";
    }

    @PostMapping("/edit/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public String updateBrand(
            @PathVariable Long id,
            @Valid @ModelAttribute("brandForm") BrandDto.BrandUpdateRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/brand/edit";
        }

        try {
            BrandDto.BrandDTO updatedBrand = brandService.updateBrand(id, request);
            redirectAttributes.addFlashAttribute("success", "Бренд успешно обновлен");
            return "redirect:/brands/admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении бренда: " + e.getMessage());
            return "redirect:/brand/admin/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBrand(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            brandService.deleteBrand(id);
            redirectAttributes.addFlashAttribute("success", "Бренд успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении бренда: " + e.getMessage());
        }

        return "redirect:/brands/admin";
    }


    @PostMapping("/{id}/banner/delete")
//    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBannerImage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            brandService.deleteBannerImage(id);
            redirectAttributes.addFlashAttribute("success", "Баннер успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении баннера: " + e.getMessage());
        }

        return "redirect:/brands/admin/edit/" + id;
    }

    // API эндпоинты для использования в AJAX запросах

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<BrandDto.BrandListDTO>> getApiPublicBrands(
            @RequestParam(required = false) Boolean active) {
        List<BrandDto.BrandListDTO> brands = brandService.getAllPublicBrands(active);
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

