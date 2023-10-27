package ru.kata.spring.boot_security.demo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public static final String COLOR_RESET = "\u001B[0m";

    // Declaring the color
    // Custom declaration
    public static final String YELLOW = "\u001B[33m";
    Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

        return user;
    }

    public User userByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public User userById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public boolean save(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            logger.info(YELLOW + "Попытка создания дубликата юзернэйма " + COLOR_RESET + user.getUsername());

            return false;
        }
        logger.info(YELLOW + "Создание нового юзера " + COLOR_RESET + user.getUsername());
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    public boolean edit(User user) {
        String usernameFromInput = user.getUsername();
        String usernameFromDB = (userRepository.findById(user.getId()).orElse(null)).getUsername();
        User userFromDB = userRepository.findByUsername(user.getUsername());


        if (usernameFromInput.equals(usernameFromDB)){
            logger.info(YELLOW + "Редактирование пользователя не меняя имени " + COLOR_RESET + user.getUsername());
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
        if (userFromDB != null) {
            logger.info(YELLOW + "Попытка создания дубликата юзернэйма v REDACTOR " + COLOR_RESET + user.getUsername());
            return false;
        }
        logger.info(YELLOW + "Создание нового юзера v REDACTOR " + COLOR_RESET + user.getUsername());
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
//    public boolean edit(User user) {
//        User userFromDB = userRepository.findByUsername(user.getUsername());
//
//        if (userFromDB != null) {
//            logger.info(YELLOW + "Попытка создания дубликата юзернэйма в редакторе " + COLOR_RESET + user.getUsername());
//            return false;
//        }
//        logger.info(YELLOW + "Создание нового юзера В редакторе " + COLOR_RESET + user.getUsername());
//        userRepository.save(user);
//        return true;
//    }

    public boolean delete(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            logger.info(YELLOW + "Удаление юзера" + COLOR_RESET);
            return true;
        }
        return false;
    }


    @PostConstruct
    private void init() {
        Role roleUser = new Role(1L, "ROLE_USER");
        Role roleAdmin = new Role(2L, "ROLE_ADMIN");
        Set<Role> userRoleSet = new HashSet<>();
        userRoleSet.add(roleUser);
        if (roleRepository.findById(1L).orElse(null) == null) {
            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
            logger.info(YELLOW + "Добавлены роли по умолчанию" + COLOR_RESET);
        }
        if (userByUsername("admin") == null) {
            User user = new User(1L, "admin", bCryptPasswordEncoder.encode("100"));
            List<Role> roleList = new ArrayList<>(roleRepository.findAll());
            Set<Role> roleSet = Set.copyOf(roleList);
            user.setRoles(roleSet);
            userRepository.save(user);
            logger.info(YELLOW + "Добавлен админ по умолчанию" + COLOR_RESET);
        }
        if (userByUsername("user") == null) {
            User user = new User(2L, "user", bCryptPasswordEncoder.encode("100"));
            user.setRoles(Collections.singleton(roleUser));
            userRepository.save(user);
            logger.info(YELLOW + "Добавлен юзер по умолчанию" + COLOR_RESET);

        }
        if (userByUsername("num1") == null) {
            User user = new User(3L, "num1", bCryptPasswordEncoder.encode("1"));
            user.setEmail("1@1");
            user.setLastName("N1");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.save(user);
            logger.info(YELLOW + "Добавлен num1" + COLOR_RESET);

        }
        if (userByUsername("num2") == null) {
            User user = new User(4L, "num2", bCryptPasswordEncoder.encode("2"));
            user.setEmail("2@2");
            user.setLastName("N2");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.save(user);
            logger.info(YELLOW + "Добавлен num2" + COLOR_RESET);

        }
        if (userByUsername("test1") == null) {
            User user = new User(5L, "test1", bCryptPasswordEncoder.encode("1"));
            user.setEmail("t1@1t");
            user.setLastName("T1");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.save(user);
            logger.info(YELLOW + "Добавлен t1" + COLOR_RESET);

        }
        if (userByUsername("test2") == null) {
            User user = new User(6L, "test2", bCryptPasswordEncoder.encode("2"));
            user.setEmail("t2@2t");
            user.setLastName("T2");
            user.setRoles(Collections.singleton(roleUser));
            userRepository.save(user);
            logger.info(YELLOW + "Добавлен t2" + COLOR_RESET);
        }
    }

}
