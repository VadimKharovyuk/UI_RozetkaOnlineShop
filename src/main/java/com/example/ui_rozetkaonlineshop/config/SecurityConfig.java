package com.example.ui_rozetkaonlineshop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").hasRole("ROLE_ADMIN")
                        .requestMatchers("/**",
                                "/login/**",
                                "/auth/**",
                                "/static/**",
                                "/css/**",
                                "/profile/**",
                                "/brands/**",
                                "/js/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                // Добавляем обработку для неавторизованных доступов
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // Для случаев, когда пользователь не имеет доступа (например, не админ)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/auth/register");
                        })
                        // Для случаев, когда пользователь не аутентифицирован
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/auth/register");
                        })
                );

        return http.build();
    }
}