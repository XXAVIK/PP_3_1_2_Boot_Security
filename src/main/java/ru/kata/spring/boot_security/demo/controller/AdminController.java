package ru.kata.spring.boot_security.demo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
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


    @GetMapping()
    public String startUp(Model model) {
        List<User> userList = userService.listUsers();
        model.addAttribute("users", userList);
        return "admin";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            logger.info(YELLOW + "Ошибки в bindingResult" + COLOR_RESET);
            return "new";
        }
        if (!userService.save(user)) {
            bindingResult.addError(error);
            logger.info(YELLOW + "Попытка дубликата - лог пишется из пост контроллера" + COLOR_RESET);
            return "new";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.userById(id));
        return "edit";
    }

    @PatchMapping("{id}/patch")
    public String edit(@ModelAttribute("user") @Valid User user, @PathVariable("id") Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info(YELLOW + "Ошибки в bindingResult" + COLOR_RESET);
            return "new";
        }
        if (!userService.edit(user)) {
            logger.info(YELLOW + "Попытка дубликата - лог пишется из патч контроллера" + COLOR_RESET);
            bindingResult.addError(error);
            return "new";
        }
        userService.edit(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
