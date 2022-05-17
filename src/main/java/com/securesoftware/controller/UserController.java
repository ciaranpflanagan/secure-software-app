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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.text.ParseException;

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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

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
            return "users/register";

        } else if (!ageVerifier(allParams.get("dob"))) {
            logger.info("Incorrect Birthday data");
            return "users/register";
        }

        else {
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
            user.setAttempts(0);
            user.setAccountLocked(false);

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
            return "login";
        }
    }

    public static boolean ageVerifier(String dob) {
        // checking structural integrity of dob
        boolean formatValidity = false;
        boolean ageChecker = false;

        try {
            // why 2008-02-2x, 20-11-02, 12012-04-05 are valid date?
            sdf.parse(dob);
            // strict mode - check 30 or 31 days, leap year
            sdf.setLenient(false);
            formatValidity = true;

        } catch (ParseException e) {
            e.printStackTrace();
            logger.info("Incorrect Birthday format");
            formatValidity = false;
        }

        if (formatValidity) {
            // getting todays date 18 years ago
            String currentDate = java.time.LocalDate.now().toString();
            String cutOffDate = java.time.LocalDate
                    .parse(currentDate)
                    .minusYears(18)
                    .toString();

            // appropriatley formatting
            String dobFormatted = dob.substring(6, 10) + "-" + dob.substring(3, 5) + "-" + dob.substring(0, 2);

            try {
                Date date1 = sdf2.parse(dobFormatted);
                Date date2 = sdf2.parse(cutOffDate);

                System.out.println("Bootsy " + date1);

                if (date1.before(date2))
                    ageChecker = true;
                else
                    logger.info("User under 18");
            } catch (Exception e) {

            }
        }

        return ageChecker;
    }

}
