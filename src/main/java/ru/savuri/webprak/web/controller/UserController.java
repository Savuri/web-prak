package ru.savuri.webprak.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.savuri.webprak.model.dao.UserDAO;
import ru.savuri.webprak.model.entity.User;

import java.util.List;

@Controller
public class UserController {
    private final UserDAO userDAO;

    UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> userList = userDAO.getAll();
        model.addAttribute("userList", userList);

        return "users";
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam(name = "userId") Long userId, Model model) {
        User user = userDAO.getById(userId);
        model.addAttribute("thisUser", user);

        return "userInfo";
    }

    @GetMapping("/userEdit")
    public String userEdit(@RequestParam(name = "userId", required = false) Long userId, Model model) {

        if (userId != null) {
            User user = userDAO.getById(userId);

            model.addAttribute("isEditUser", true);
            model.addAttribute("thisUser", user);
        } else {
            model.addAttribute("isEditUser", false);
            model.addAttribute("thisUser", null);
        }

        return "userEdit";
    }

    @PostMapping("/userSave")
    public String userSave(@RequestParam(name = "userId", required = false) Long userId,
                           @RequestParam(name = "fullName") String fullName,
                           @RequestParam(name = "phoneNumber") String phoneNumber,
                           @RequestParam(name = "address") String address,
                           @RequestParam(name = "email") String email,
                           Model model) {
        if (userId == null) {
            User user = new User(fullName, phoneNumber, address, email);
            userDAO.save(user);

            return String.format("redirect:/userInfo?userId=%d", user.getId());
        } else {
            User user = userDAO.getById(userId);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            user.setEmail(email);

            userDAO.update(user);

            return String.format("redirect:/userInfo?userId=%d", user.getId());
        }
    }

    @PostMapping("/userDelete")
    public String userDelete(@RequestParam(name = "userId") Long userId) {
        userDAO.delete(userDAO.getById(userId));

        return "redirect:/users";
    }
}
