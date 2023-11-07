package ru.kata.spring.boot_security.demo.init;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.*;

import static ru.kata.spring.boot_security.demo.service.UserService.COLOR_RESET;
import static ru.kata.spring.boot_security.demo.service.UserService.YELLOW;

@Component
@RequiredArgsConstructor
public class DatabaseInitialization {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger logger = LoggerFactory.getLogger(DatabaseInitialization.class);


    public User userByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    //      Создание пользователей и ролей по умолчанию

    @PostConstruct
    void init() {

        Role roleUser = new Role(1L, "ROLE_USER");
        Role roleAdmin = new Role(2L, "ROLE_ADMIN");
        Set<Role> userRoleSet = new HashSet<>();
        userRoleSet.add(roleUser);
        if (roleRepository.findById(1L) == null) {
            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
            logger.info(YELLOW + "Добавлены роли по умолчанию" + COLOR_RESET);
        }

        if (userByUsername("admin") == null) {
            User user = new User(1L, "admin", bCryptPasswordEncoder.encode("100"));
            List<Role> roleList = new ArrayList<>(roleRepository.findAll());
            Set<Role> roleSet = Set.copyOf(roleList);
            user.setRoles(roleSet);
            userRepository.edit(user);
            logger.info(YELLOW + "Добавлен админ по умолчанию" + COLOR_RESET);
        }
        if (userByUsername("user") == null) {
            User user = new User(2L, "user", bCryptPasswordEncoder.encode("100"));
            user.setRoles(Collections.singleton(roleUser));
            userRepository.edit(user);
            logger.info(YELLOW + "Добавлен юзер по умолчанию" + COLOR_RESET);

        }
        if (userByUsername("num1") == null) {
            User user = new User(3L, "num1", bCryptPasswordEncoder.encode("1"));
            user.setEmail("1@1");
            user.setLastName("N1");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.edit(user);
            logger.info(YELLOW + "Добавлен num1" + COLOR_RESET);

        }
        if (userByUsername("num2") == null) {
            User user = new User(4L, "num2", bCryptPasswordEncoder.encode("2"));
            user.setEmail("2@2");
            user.setLastName("N2");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.edit(user);
            logger.info(YELLOW + "Добавлен num2" + COLOR_RESET);

        }
        if (userByUsername("test1") == null) {
            User user = new User(5L, "test1", bCryptPasswordEncoder.encode("1"));
            user.setEmail("t1@1t");
            user.setLastName("T1");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.edit(user);
            logger.info(YELLOW + "Добавлен t1" + COLOR_RESET);

        }
        if (userByUsername("test2") == null) {
            User user = new User(6L, "test2", bCryptPasswordEncoder.encode("2"));
            user.setEmail("t2@2t");
            user.setLastName("T2");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.edit(user);
            logger.info(YELLOW + "Добавлен t2" + COLOR_RESET);
        }
    }

}
