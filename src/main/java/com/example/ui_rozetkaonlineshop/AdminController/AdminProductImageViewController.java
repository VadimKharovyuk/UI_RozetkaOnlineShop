package com.example.ui_rozetkaonlineshop.AdminController;

import com.example.ui_rozetkaonlineshop.DTO.ProductImage.ProductImageDto;
import com.example.ui_rozetkaonlineshop.service.ProductImageClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/product-images")
@RequiredArgsConstructor
@Slf4j
public class AdminProductImageViewController {

    private final ProductImageClientService imageService;

    @GetMapping("/product/{productId}")
    public String getProductImages(@PathVariable Long productId, Model model) {
        ProductImageDto.ListResponse images = imageService.getProductImages(productId);
        model.addAttribute("productImages", images);
        model.addAttribute("productId", productId);

        return "admin/product-image/image-list";
    }

    @GetMapping("/{imageId}")
    public String getImageDetails(@PathVariable Long imageId, Model model) {
        ProductImageDto.Response image = imageService.getImageById(imageId);
        if (image == null) {
            return "admin/product-image/not-found";
        }

        model.addAttribute("image", image);
        return "admin/product-image/image-details";
    }

    @GetMapping("/upload/{productId}")
    public String showUploadForm(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "admin/product-image/upload-form";
    }

    @PostMapping("/upload")
    public String uploadImage(
            @RequestParam("productId") Long productId,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "imageType", required = false) String imageType,
            @RequestParam(value = "alt", required = false) String alt,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            RedirectAttributes redirectAttributes) {

        ProductImageDto.Response uploaded = imageService.uploadImage(
                productId, imageFile, imageType, alt, sortOrder);

        if (uploaded != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Изображение успешно загружено");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка загрузки изображения");
        }

        return "redirect:/admin/product-images/product/" + productId;
    }

    @GetMapping("/{imageId}/edit")
    public String showEditForm(@PathVariable Long imageId, Model model) {
        ProductImageDto.Response image = imageService.getImageById(imageId);
        if (image == null) {
            return "admin/product-image/not-found";
        }

        model.addAttribute("image", image);
        return "admin/product-image/edit-form";
    }

    @PostMapping("/{imageId}/update")
    public String updateImage(
            @PathVariable Long imageId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageType", required = false) String imageType,
            @RequestParam(value = "alt", required = false) String alt,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            RedirectAttributes redirectAttributes) {

        ProductImageDto.Response updated = imageService.updateImage(
                imageId, imageFile, imageType, alt, sortOrder);

        if (updated != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Изображение успешно обновлено");
            return "redirect:/admin/product-images/product/" + updated.getProductId();
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка обновления изображения");
            return "redirect:/admin/product-images/" + imageId + "/edit";
        }
    }

    @GetMapping("/{imageId}/delete")
    public String deleteImage(
            @PathVariable Long imageId,
            @RequestParam Long productId,
            RedirectAttributes redirectAttributes) {

        boolean deleted = imageService.deleteImage(imageId);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Изображение успешно удалено");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка удаления изображения");
        }

        return "redirect:/admin/product-images/product/" + productId;
    }

    @GetMapping("/{imageId}/set-main")
    public String setMainImage(
            @PathVariable Long imageId,
            @RequestParam Long productId,
            RedirectAttributes redirectAttributes) {

        ProductImageDto.Response mainImage = imageService.setMainImage(imageId);

        if (mainImage != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Главное изображение успешно установлено");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка установки главного изображения");
        }

        return "redirect:/admin/product-images/product/" + productId;
    }

    @GetMapping("/reorder/{productId}")
    public String showReorderForm(@PathVariable Long productId, Model model) {
        ProductImageDto.ListResponse images = imageService.getProductImages(productId);
        model.addAttribute("productImages", images);
        model.addAttribute("productId", productId);

        return "admin/product-image/reorder-form";
    }

    @PostMapping("/reorder")
    public String reorderImages(
            @RequestParam Long productId,
            @RequestParam List<Long> imageIds,
            RedirectAttributes redirectAttributes) {

        boolean updated = imageService.updateImageOrder(productId, imageIds);

        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Порядок изображений успешно обновлен");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка обновления порядка изображений");
        }

        return "redirect:/admin/product-images/product/" + productId;
    }
}