package com.securesoftware.controller;

import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.model.VaccinationSlot;
import com.securesoftware.repository.UserRepository;
import com.securesoftware.repository.VaccinationAppointmentRepository;
import com.securesoftware.repository.ActivityRepository;
import com.securesoftware.model.Activity;
import com.securesoftware.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/hse-admin")
public class HseController {
    /*
        admins should be able to:
         * View all appointments
         * Mark appointment as administered 
    */

    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @GetMapping("/all-appointments")
    public String viewAppointments(Model model) {
        //Add preventative measures for non-admins
        /*
        ****************************************************
        */
        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        model.addAttribute("allAppointments", allAppointments);

        return "hse/appointments";
    }

    @PostMapping("/edit-appointments")
    public String editAppointments(@RequestParam Map<String, String> allParams, Model model){
        String idAsString = "0"; // Note this will be a parameter
        int id = Integer.parseInt(idAsString);

        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        VaccinationAppointment appointmentToEdit = allAppointments.get(id);

        // Here we will edit the appointment using parameters and the setter



        // Updating the vaccination activity table
        Activity updatedActivity = new Activity();
        updatedActivity.setDate(appointmentToEdit.getTimeSlot());
        updatedActivity.setUser(appointmentToEdit.getUser());
        updatedActivity.setActivityType("Vaccine Administered");
        activityRepository.save(updatedActivity);

        return "hse/appointments";
    }

}
