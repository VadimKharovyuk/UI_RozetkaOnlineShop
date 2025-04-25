package com.example.ui_rozetkaonlineshop.service;



import com.example.ui_rozetkaonlineshop.DTO.user.*;
import com.example.ui_rozetkaonlineshop.FeignClient.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthServiceClient authServiceClient;
    public UserDto registerUser(RegisterRequest request) {
        try {
            // Логирование перед вызовом
            log.info("Attempting to register user: {}", request);

            ResponseEntity<UserDto> response = authServiceClient.registerUser(request);

            // Логирование результата
            log.info("Registration response: {}", response);
            return response.getBody();
        } catch (Exception e) {
            // Детальное логирование ошибки
            log.error("Registration failed", e);

            // Дополнительная диагностика
            log.error("Exception class: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());

            // Трассировка стека
            e.printStackTrace();

            throw new RuntimeException("Registration service is unavailable", e);
        }
    }

    public AuthResponse authenticateUser(AuthRequest request) {
        ResponseEntity<AuthResponse> response = authServiceClient.authenticateUser(request);
        return response.getBody();
    }

    public UserDto getCurrentUser(String token) {
        ResponseEntity<UserDto> response = authServiceClient.getCurrentUser("Bearer " + token);
        return response.getBody();
    }
}
