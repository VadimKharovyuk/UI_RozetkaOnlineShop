package com.example.ui_rozetkaonlineshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/profile")
@Controller
public class ProfileController {

    @GetMapping
    public String  profile(Model model){
        return "/client/auth/profile";
    }
}
