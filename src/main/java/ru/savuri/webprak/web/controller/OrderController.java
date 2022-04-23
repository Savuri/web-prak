package ru.savuri.webprak.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    @GetMapping("/orderInfo")
    public String orderInfo() {
        return "orderInfo";
    }

    @GetMapping("/orderCreate")
    public String orderCreate() {
        return "orderCreate";
    }
}
