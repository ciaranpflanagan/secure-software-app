package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.User;
import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.model.VaccinationSlot;
import com.securesoftware.repository.ActivityRepository;
import com.securesoftware.repository.UserRepository;
import com.securesoftware.repository.VaccinationAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/activities")
public class ActivityController {

    @Autowired
    ActivityRepository ActivityRepository;

    @Autowired
    static UserRepository userRepository;

    @RequestMapping(value = "/recent-activity", method = RequestMethod.GET)
    public String getRecentActivity(Model model, Authentication authentication, HttpServletRequest request){
        String userId = (String) authentication.getCredentials();
        long temp = 4;
        User user = userRepository.getById(temp);
        return null;
    }

    public static void main(String[] args) {

        //Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //String email = "brianbyrne@gmail.com";
        //String userId = (String) authentication.getCredentials();
        long temp = 4;
        //User user = userRepository.getById(temp);

        System.out.println(temp);
    }
    
}
