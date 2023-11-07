package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;

public interface RoleRepository {
    void save(Role role);

    void edit(Role role);

    void deleteById(long id);

    Role findById(long id);

    List<Role> findAll();
}