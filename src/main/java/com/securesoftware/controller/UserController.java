package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.User;
import com.securesoftware.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserRepository UserRepository;

    @RequestMapping("/list")
    public String viewHomePage(Model model) {
        List<User> listBooks = UserRepository.findAll();
        model.addAttribute("listBooks", listBooks);
        
        return "hello";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        UserRepository.save(user);

        return viewHomePage(model);
    }
}
