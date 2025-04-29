package com.example.ui_rozetkaonlineshop.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Получаем текущий запрос
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                HttpSession session = request.getSession(false);

                if (session != null) {
                    // Получаем токен из сессии
                    String token = (String) session.getAttribute("token");

                    if (token != null && !token.isEmpty()) {
                        log.debug("Using token from session");
                        requestTemplate.header("Authorization", "Bearer " + token);
                    } else {
                        log.warn("Токен не найден в сессии");
                    }
                } else {
                    log.warn("Сессия не найдена");
                }
            } else {
                log.warn("RequestAttributes не найдены");
            }
        };
    }
    /**
     * Конфигурация для поддержки отправки multipart/form-data через Feign.
     * Необходимо для загрузки файлов.
     */
    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}