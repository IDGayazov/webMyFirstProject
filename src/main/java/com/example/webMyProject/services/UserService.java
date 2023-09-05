package com.example.webMyProject.services;


import com.example.webMyProject.models.Role;
import com.example.webMyProject.models.User;
import com.example.webMyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if(user != null){
            System.out.println(user.getRoles().toString());
            return user;
        }
        else
            throw new UsernameNotFoundException("can't find username with such login/email");
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user, String login, Map<String, String> form) {
        user.setLogin(login);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.getRoles().clear();

        for(String key: form.keySet()){
            if(roles.contains(key))
                user.getRoles().add(Role.valueOf(key));
        }
        userRepository.save(user);
    }

    public void updateProfile(User user, String email, String password) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = email != null && !userEmail.equals(email);
        if(isEmailChanged){
            user.setEmail(email);
        }
        if(password != null)
            user.setPassword(password);

        userRepository.save(user);
    }
}
