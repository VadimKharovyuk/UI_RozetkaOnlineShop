package com.example.ui_rozetkaonlineshop.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        // Логирование деталей ошибки
        log.error("Feign Error Decoder");
        log.error("Method Key: {}", methodKey);
        log.error("Response Status: {}", response.status());

        try {
            // Попытка прочитать тело ответа для более детальной информации
            String responseBody = response.body() != null
                    ? new String(response.body().asInputStream().readAllBytes())
                    : "No response body";
            log.error("Response Body: {}", responseBody);
        } catch (Exception e) {
            log.error("Error reading response body", e);
        }

        return switch (response.status()) {
            case 400 -> new BadRequestException("Bad Request - Invalid input");
            case 401 -> new UnauthorizedException("Unauthorized - Authentication required");
            case 403 -> new ForbiddenException("Forbidden - You don't have permission");
            case 404 -> new ResourceNotFoundException("Not Found - Resource does not exist");
            case 409 -> new ConflictException("Conflict - Resource already exists");
            case 500 -> new InternalServerErrorException("Internal Server Error");
            case 503 -> new ServiceUnavailableException("Service Temporarily Unavailable");
            default -> new RuntimeException("Unknown error occurred. Status: " + response.status());
        };
    }

    // Пользовательские исключения для более точной обработки
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class ForbiddenException extends RuntimeException {
        public ForbiddenException(String message) {
            super(message);
        }
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class ConflictException extends RuntimeException {
        public ConflictException(String message) {
            super(message);
        }
    }

    public static class InternalServerErrorException extends RuntimeException {
        public InternalServerErrorException(String message) {
            super(message);
        }
    }

    public static class ServiceUnavailableException extends RuntimeException {
        public ServiceUnavailableException(String message) {
            super(message);
        }
    }
}