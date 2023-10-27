package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    FieldError error = new FieldError("username", "username", "Username already exists");
    public static final String COLOR_RESET = "\u001B[0m";

    // Declaring the color
    // Custom declaration
    public static final String YELLOW = "\u001B[33m";
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String startUp(Model model, Principal principal) {
        model.addAttribute("user", userService.userByUsername(principal.getName()));
        return "user";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            logger.info(YELLOW + "Ошибки в bindingResult" + COLOR_RESET);
            return "registration";
        }
        if (!userService.save(user)) {
            bindingResult.addError(error);
            logger.info(YELLOW + "Попытка дубликата - лог пишется из пост контроллера" + COLOR_RESET);
            return "registration";
        }
        userService.save(user);
        return "redirect:/user";
    }

    @GetMapping("/registration")
    public String newUser(@ModelAttribute("user") User user) {
        return "registration";
    }
}
