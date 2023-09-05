package com.example.webMyProject.controllers;

import com.example.webMyProject.models.Role;
import com.example.webMyProject.models.User;
import com.example.webMyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user, Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          Model model){
        if(bindingResult.hasErrors()){
            var errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        else{
            User userFromDb1 = userRepository.findByLogin(user.getLogin());
            User userFromDb2 = userRepository.findByEmail(user.getEmail());
            if(userFromDb1 != null || userFromDb2 != null) {
                model.addAttribute("message", "Пользователь с таким email/login уже существует!");
                return "registration";
            }
            System.out.println(user.getPassword());
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/login";
        }
    }
}
