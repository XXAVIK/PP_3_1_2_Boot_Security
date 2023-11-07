package ru.kata.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
import java.security.Principal;
import static ru.kata.spring.boot_security.demo.controller.AdminController.error;
import static ru.kata.spring.boot_security.demo.service.UserService.COLOR_RESET;
import static ru.kata.spring.boot_security.demo.service.UserService.YELLOW;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String startUp(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "user";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            log.info(YELLOW + "Ошибки в bindingResult - registration" + COLOR_RESET);
            return "registration";
        }
        if (!userService.save(user)) {
            bindingResult.addError(error);
            log.info(YELLOW + "Попытка дубликата - лог пишется из пост контроллера - registration" + COLOR_RESET);
            return "registration";
        }
        return "redirect:/user";
    }

    @GetMapping("/registration")
    public String newUser(@ModelAttribute("user") User user) {
        return "registration";
    }
}
