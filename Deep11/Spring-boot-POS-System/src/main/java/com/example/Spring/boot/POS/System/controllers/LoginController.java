package com.example.Spring.boot.POS.System.controllers;



import com.example.Spring.boot.POS.System.model.User; // Ensure you have the correct package for the User model
import com.example.Spring.boot.POS.System.repository.UserRepository; // Ensure you have the correct package for the UserRepository
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    // You can add a UserRepository or other required services if needed
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for UserRepository and PasswordEncoder
    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Optionally, add a message for registered users
        model.addAttribute("registered", model.containsAttribute("registered"));
        return "login"; // This should match the name of your login view template
    }

    @PostMapping("/login") // This assumes you handle the login form submission with POST (OAuth2/CSRF can affect this)
    public String loginUser(@ModelAttribute("user") User user, Model model, BindingResult result, Authentication authentication) {
        // Here you can validate credentials, display errors or proceed if authentication is required by security config
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home"; // Redirect to home if already authenticated
        }

        // Optionally validate the user input or handle errors
        // Add user login logic if not using Spring Security's built-in authentication

        return "login"; // Return to the login page if authentication fails
    }
}