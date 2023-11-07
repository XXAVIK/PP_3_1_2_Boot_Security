package ru.kata.spring.boot_security.demo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    // Declaring the color
    public static final String COLOR_RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

        return user;
    }
    public User findByUsername(String name){
        return userRepository.findByUsername(name);
    }

    public User userById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean save(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            log.info(YELLOW + "Попытка создания дубликата юзернэйма " + COLOR_RESET + user.getUsername());

            return false;
        }

        log.info(YELLOW + "Создание нового юзера " + COLOR_RESET + user.getUsername());
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    @Transactional
    public boolean edit(User user) {
        String usernameFromInput = user.getUsername(); //Получаю имя из инпута
        String usernameFromDB = (userRepository.       //Получаю имя юзера с тем же айди из БД
                findById(user.getId()).getUsername());
        //Получаю всего пользователя по имени
        User userFromDB = userRepository.findByUsername(usernameFromInput);

        //Проверяю существует ли юзернэйм из инпута в БД
        //Если уже существует, значит это либо пользователь без изменений, либо имя оставили без изменений
        if (usernameFromInput.equals(usernameFromDB)) {
            log.info(YELLOW + "Редактирование пользователя не меняя имени " + COLOR_RESET + user.getUsername());
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.edit(user);
            return true;
        }
        //Если новое имя из инпута совпадает со старым, то оно попадет в этот фильтр
        if (userFromDB != null) {
            log.info(YELLOW + "Попытка создания дубликата юзернэйма v REDACTOR " + COLOR_RESET + user.getUsername());
            return false;
        }
        //Если юзернейм новый, но поменялись любые другие поля, то пользователь попадает сюда
        log.info(YELLOW + "Создание нового юзера v REDACTOR " + COLOR_RESET + user.getUsername());
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.edit(user);

        //Я чет не смог придумать лучше способа отлавливать случаи,когда в редакторе вводится уже сущ имя

        return true;
    }

    @Transactional
    public boolean delete(Long userId) {
        if (userRepository.findById(userId) != null) {
            userRepository.deleteById(userId);
            log.info(YELLOW + "Удаление юзера" + COLOR_RESET);
            return true;
        }
        return false;
    }
}
