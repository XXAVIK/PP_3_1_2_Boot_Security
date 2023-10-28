package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);
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
            logger.info(YELLOW + "Ошибки в bindingResult - registration" + COLOR_RESET);
            return "registration";
        }
        if (!userService.save(user)) {
            bindingResult.addError(error);
            logger.info(YELLOW + "Попытка дубликата - лог пишется из пост контроллера - registration" + COLOR_RESET);
            return "registration";
        }
        return "redirect:/user";
    }

    @GetMapping("/registration")
    public String newUser(@ModelAttribute("user") User user) {
        return "registration";
    }
}
