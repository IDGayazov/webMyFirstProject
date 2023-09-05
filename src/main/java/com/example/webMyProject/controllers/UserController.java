package com.example.webMyProject.controllers;

import com.example.webMyProject.models.Role;
import com.example.webMyProject.models.User;
import com.example.webMyProject.repository.UserRepository;
import com.example.webMyProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model){
        Optional<User> usr = userService.findById(id);
        User user = usr.stream().findFirst().stream().toList().get(0);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-editor";
    }

    @PostMapping
    public String editUser(
            @RequestParam String login,
            @RequestParam Map<String, String> form,
            @RequestParam("user_id") User user){
        userService.saveUser(user, login, form);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("login", user.getLogin());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Model model,
                                @AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email
    ){
        userService.updateProfile(user, email, password);
        return "redirect:/user/profile";
    }
}
