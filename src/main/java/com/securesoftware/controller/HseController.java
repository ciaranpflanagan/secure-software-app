package com.securesoftware.controller;

import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.model.VaccinationSlot;
import com.securesoftware.repository.UserRepository;
import com.securesoftware.repository.VaccinationAppointmentRepository;

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

}
