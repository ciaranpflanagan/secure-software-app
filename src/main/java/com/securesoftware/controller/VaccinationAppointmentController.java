package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.repository.VaccinationAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/vaccinationappointments")
public class VaccinationAppointmentController {
    
    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;
}
