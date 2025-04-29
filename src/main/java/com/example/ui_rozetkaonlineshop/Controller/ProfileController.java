package com.example.ui_rozetkaonlineshop.Controller;

import com.example.ui_rozetkaonlineshop.DTO.user.UserDto;
import com.example.ui_rozetkaonlineshop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("/profile")
@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final AuthService authService;



    @GetMapping()
    public String showProfile(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("token");
        if (token == null) {
            return "redirect:/auth/register";
        }

        UserDto userDto = authService.getCurrentUser(token);
        model.addAttribute("user", userDto);
        System.out.println(userDto.getRoles() +" зашел в аканут ");
        return "client/profile/profile";
    }
}