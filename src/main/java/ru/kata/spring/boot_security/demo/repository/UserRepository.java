package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserRepository {
    void save(User user);

    void edit(User user);

    void deleteById(long id);

    User findById(long id);

    User findByUsername(String username);

    List<User> findAll();
}
