package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.Activity;
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
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTMLDocument.Iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(path = "/activities")
public class ActivityController {

    @Autowired
    ActivityRepository ActivityRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/recent-activity", method = RequestMethod.GET)
    public String getRecentActivity(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        
        // Getting active user
        User user = userRepository.findByEmail(email);
        Set<Activity> activitiesOfUser = user.getActivities();
        List<Activity> activitiesList = new ArrayList<>(activitiesOfUser);
        // Getting the last activity of the user
        Activity latestActivity = activitiesList.get(activitiesList.size() - 1);

        model.addAttribute("userId", latestActivity.getId());
        model.addAttribute("latestActivity", latestActivity.getActivityType());


        return "activities/recent_activity";
    }

}
