package com.example.webMyProject.repository;

import com.example.webMyProject.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
    User findByEmail(String email);
}
