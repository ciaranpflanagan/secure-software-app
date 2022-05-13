package com.securesoftware.controller;

import com.securesoftware.model.Activity;
import com.securesoftware.model.User;
import com.securesoftware.repository.ActivityRepository;
import com.securesoftware.repository.RoleRepository;
import com.securesoftware.repository.UserRepository;
import com.securesoftware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.BasicConfigurator; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserRepository UserRepository;
    @Autowired
    RoleRepository RoleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ActivityRepository activityRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Bean
    public UserService userService() {
        return new UserService(UserRepository, RoleRepository, bCryptPasswordEncoder); // I don't think this is correct
    }

    // @RequestMapping("/list")
    // public String viewHomePage(Model model) {
    // List<User> listBooks = UserRepository.findAll();
    // model.addAttribute("listBooks", listBooks);

    // return "hello";
    // }

    @GetMapping("/register")
    public String register() {
        return "users/register";
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam Map<String, String> allParams) {
        BasicConfigurator.configure();  
        logger.info("User attempting to register"); 
        User userExists = userService().findUserByEmail(allParams.get("email"));
        if (userExists != null) {
            logger.info("User already exists");
            // Error - need to handle this
        } else {
            // Create user
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
            userService().saveUser(user);

            // Updating the vaccination activity table
            Activity updatedActivity = new Activity();
            updatedActivity.setDate(java.time.LocalDate.now().toString());
            updatedActivity.setUser(user);
            updatedActivity.setActivityType("Account Created");
            activityRepository.save(updatedActivity);
            BasicConfigurator.configure();  
            logger.info("User Successfully created");
        }

        return "login";
    }
}
