package ru.savuri.webprak.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/userInfo")
    public String userInfo() {
        return "userInfo";
    }

    @GetMapping("/userEdit")
    public String userEdit() {
        return "userEdit";
    }
}
