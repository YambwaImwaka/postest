package com.example.Spring.boot.POS.System.controllers;

import com.example.Spring.boot.POS.System.model.User; // Ensure you have the correct package for the User model
//import com.pos.system.repositories.UserRepository; // Ensure you have the correct package for the UserRepository
import com.example.Spring.boot.POS.System.repository.UserRepository;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result) {
        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
        }

        if (result.hasErrors()) {
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add("CASHIER"); // Default role
        userRepository.save(user);

        return "redirect:/login?registered";
    }
}
