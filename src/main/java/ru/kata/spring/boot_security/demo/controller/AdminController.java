package ru.kata.spring.boot_security.demo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.kata.spring.boot_security.demo.service.UserService.COLOR_RESET;
import static ru.kata.spring.boot_security.demo.service.UserService.YELLOW;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    public static final FieldError error = new FieldError("username", "username", "Username already exists");
    private final UserService userService;


    @GetMapping()
    public String startUp(Model model) {
        List<User> userList = userService.listUsers();
        model.addAttribute("users", userList);
        return "admin";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info(YELLOW + "Ошибки в bindingResult" + COLOR_RESET);
            return "new";
        }
        if (!userService.save(user)) {
            bindingResult.addError(error);
            log.info(YELLOW + "Попытка дубликата - лог пишется из пост контроллера" + COLOR_RESET);
            return "new";
        }
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
    public String edit(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            log.info(YELLOW + "Ошибки в bindingResult - патч контроллер" + COLOR_RESET);
            return "edit";
        }
        if (!userService.edit(user)) {
            log.info(YELLOW + "Попытка дубликата - лог пишется из патч контроллера" + COLOR_RESET);
            bindingResult.addError(error);
            return "edit";
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
