package com.securesoftware.controller;

import com.securesoftware.model.Activity;
import com.securesoftware.model.User;
import com.securesoftware.repository.ActivityRepository;
import com.securesoftware.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
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
        model.addAttribute("activity", latestActivity);

        return "activities/recent_activity";
    }

}
