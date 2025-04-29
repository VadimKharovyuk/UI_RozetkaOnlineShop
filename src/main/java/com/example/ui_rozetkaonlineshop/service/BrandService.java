package com.example.ui_rozetkaonlineshop.service;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import com.example.ui_rozetkaonlineshop.FeignClient.ProductServiceClient;
import com.example.ui_rozetkaonlineshop.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final ProductServiceClient productServiceClient;

    // Методы для работы с публичными эндпоинтами брендов

    public List<BrandDto.BrandListDTO> getAllPublicBrands(Boolean active) {
        try {
            ResponseEntity<List<BrandDto.BrandListDTO>> response = productServiceClient.getAllPublicBrands(active);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении публичного списка брендов: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public PageResponse<BrandDto.BrandListDTO> getPaginatedPublicBrands(int page, int size, String sortBy, String sortDirection) {
        try {
            ResponseEntity<PageResponse<BrandDto.BrandListDTO>> response =
                    productServiceClient.getPaginatedPublicBrands(page, size, sortBy, sortDirection);
            return response.getBody() != null ? response.getBody() : new PageResponse<>();
        } catch (Exception e) {
            log.error("Ошибка при получении постраничного списка брендов: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    public List<BrandDto.BrandListDTO> searchPublicBrands(String query) {
        try {
            ResponseEntity<List<BrandDto.BrandListDTO>> response = productServiceClient.searchPublicBrands(query);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при поиске брендов: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<BrandDto.BrandListDTO> getPublicPremiumBrands() {
        try {
            ResponseEntity<List<BrandDto.BrandListDTO>> response = productServiceClient.getPublicPremiumBrands();
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении премиум-брендов: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<BrandDto.BrandDTO> getPublicBrandById(Long id) {
        try {
            ResponseEntity<BrandDto.BrandDTO> response = productServiceClient.getPublicBrandById(id);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Ошибка при получении публичной информации о бренде по ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<BrandDto.BrandDTO> getPublicBrandBySlug(String slug) {
        try {
            ResponseEntity<BrandDto.BrandDTO> response = productServiceClient.getPublicBrandBySlug(slug);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Ошибка при получении публичной информации о бренде по slug {}: {}", slug, e.getMessage());
            return Optional.empty();
        }
    }

    // Методы для работы с административными эндпоинтами брендов

    public List<BrandDto.BrandListDTO> getAllAdminBrands(Boolean active) {
        try {
            ResponseEntity<List<BrandDto.BrandListDTO>> response = productServiceClient.getAllAdminBrands(active);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Ошибка при получении административного списка брендов: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public BrandDto.BrandDTO createBrand(BrandDto.BrandCreateRequest request) {
        try {
            ResponseEntity<BrandDto.BrandDTO> response = productServiceClient.createBrand(request);
            if (response.getBody() == null) {
                throw new RuntimeException("Не удалось создать бренд: пустой ответ от сервера");
            }
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при создании бренда: {}", e.getMessage());
            throw new RuntimeException("Не удалось создать бренд: " + e.getMessage());
        }
    }

    public BrandDto.BrandDTO updateBrand(Long id, BrandDto.BrandUpdateRequest request) {
        try {
            ResponseEntity<BrandDto.BrandDTO> response = productServiceClient.updateBrand(id, request);
            if (response.getBody() == null) {
                throw new RuntimeException("Не удалось обновить бренд: пустой ответ от сервера");
            }
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при обновлении бренда {}: {}", id, e.getMessage());
            throw new RuntimeException("Не удалось обновить бренд: " + e.getMessage());
        }
    }

    public void deleteBrand(Long id) {
        try {
            ResponseEntity<Void> response = productServiceClient.deleteBrand(id);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Не удалось удалить бренд: код ответа " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении бренда {}: {}", id, e.getMessage());
            throw new RuntimeException("Не удалось удалить бренд: " + e.getMessage());
        }
    }

    public BrandDto.BrandDTO uploadBannerImage(Long id, MultipartFile file) {
        try {
            ResponseEntity<BrandDto.BrandDTO> response = productServiceClient.uploadBannerImage(id, file);
            if (response.getBody() == null) {
                throw new RuntimeException("Не удалось загрузить баннер: пустой ответ от сервера");
            }
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при загрузке баннера для бренда {}: {}", id, e.getMessage());
            throw new RuntimeException("Не удалось загрузить баннер: " + e.getMessage());
        }
    }

    public void deleteBannerImage(Long id) {
        try {
            ResponseEntity<Void> response = productServiceClient.deleteBannerImage(id);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Не удалось удалить баннер: код ответа " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении баннера для бренда {}: {}", id, e.getMessage());
            throw new RuntimeException("Не удалось удалить баннер: " + e.getMessage());
        }
    }
}