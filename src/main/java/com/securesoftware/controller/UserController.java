package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.User;
import com.securesoftware.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/register")
    public String register() {
        return "users/register";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam Map<String,String> allParams) {
        // System.out.println(user.getAddress());
        
        System.out.println("Parameters are " + allParams.entrySet());

        User user = new User();
        user.setFirstName(allParams.get("first_name"));
        user.setLastName(allParams.get("last_name"));
        user.setDOB(allParams.get("dob"));
        user.setPPSNumber(allParams.get("pps"));
        user.setAddress(allParams.get("address"));
        user.setPhoneNumber(allParams.get("phone_number"));
        user.setEmail(allParams.get("email"));
        user.setNationality(allParams.get("nationality"));
        user.setPassword(allParams.get("password"));

        // Save user
        UserRepository.save(user);

        return "login";
    }
}
