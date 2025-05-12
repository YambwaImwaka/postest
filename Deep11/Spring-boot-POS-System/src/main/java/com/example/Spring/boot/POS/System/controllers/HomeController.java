package com.example.Spring.boot.POS.System.controllers;

import org.springframework.security.core.Authentication; // For authentication object
import org.springframework.stereotype.Controller; // For the Controller annotation
import org.springframework.ui.Model; // For the Model object
import org.springframework.web.bind.annotation.GetMapping; // For the GetMapping annotation

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "home";
    }
}