package ru.savuri.webprak.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoodController {

    @GetMapping("/goods")
    public String goods() {
        return "goods";
    }

    @GetMapping("/goodInfo")
    public String goodInfo() {
        return "goodInfo";
    }

    @GetMapping("/goodEdit")
    public String goodEdit() {
        return "goodEdit";
    }
}
