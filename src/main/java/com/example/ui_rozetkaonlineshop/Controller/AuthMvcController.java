package com.example.ui_rozetkaonlineshop.Controller;
import com.example.ui_rozetkaonlineshop.DTO.user.*;
import com.example.ui_rozetkaonlineshop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthMvcController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("authRequest", new AuthRequest());

        return "client/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute RegisterRequest registerRequest,
            RedirectAttributes redirectAttributes) {

        UserDto userDto = authService.registerUser(registerRequest);
        redirectAttributes.addFlashAttribute("message", "Registration successful!");
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "client/auth/login";
    }

    @PostMapping("/authenticateUser")
    public String loginUser(
            @ModelAttribute AuthRequest authRequest,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        AuthResponse response = authService.authenticateUser(authRequest);
        session.setAttribute("token", response.getToken());
        redirectAttributes.addFlashAttribute("message", "Login successful!");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("token");
        if (token == null) {
            return "redirect:/auth/login";
        }

        UserDto userDto = authService.getCurrentUser(token);
        model.addAttribute("user", userDto);
        return "client/auth/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("token");
        return "redirect:/auth/login";
    }
}