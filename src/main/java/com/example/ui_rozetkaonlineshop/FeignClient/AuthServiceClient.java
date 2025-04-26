package com.example.ui_rozetkaonlineshop.FeignClient;


import com.example.ui_rozetkaonlineshop.DTO.user.AuthRequest;
import com.example.ui_rozetkaonlineshop.DTO.user.AuthResponse;
import com.example.ui_rozetkaonlineshop.DTO.user.RegisterRequest;
import com.example.ui_rozetkaonlineshop.DTO.user.UserDto;
import com.example.ui_rozetkaonlineshop.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(
        name = "auth-service",
        configuration = FeignConfig.class
)

public interface AuthServiceClient {

    @PostMapping("/api/auth/register")
    ResponseEntity<UserDto> registerUser(@RequestBody RegisterRequest registerRequest);

    @PostMapping("/api/auth/login")
    ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest);

    @GetMapping("/api/auth/user")
    ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token);
}