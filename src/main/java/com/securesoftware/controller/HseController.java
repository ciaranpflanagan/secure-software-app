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

import org.apache.log4j.BasicConfigurator;  
import org.apache.log4j.LogManager;  
import org.apache.log4j.Logger;  

@Controller
@RequestMapping(path = "/hse-admin")
public class HseController {
    /*
     * admins should be able to:
     * View all appointments
     * Mark appointment as administered
     */

    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    private static final Logger logger = LogManager.getLogger(HseController.class);  

    @GetMapping("/all-appointments")
    public String viewAppointments(Model model) {
        BasicConfigurator.configure();
        logger.info("Admin has viewed all appointments");
        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        model.addAttribute("allAppointments", allAppointments);

        return "hse/appointments";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(value = "id") Long appointmentId) {
        BasicConfigurator.configure();
        logger.info("Admin has updated an appointment");
        String[] times = {
                "09:00",
                "09:15",
                "09:30",
                "09:45",
                "10:00",
                "10:15",
                "10:30",
                "10:45",
                "11:00",
                "11:15",
                "11:30",
                "11:45",
                "12:00",
                "12:15",
                "12:30",
                "12:45",
                "13:00",
                "13:15",
                "13:30",
                "13:45",
                "14:00",
                "14:15",
                "14:30",
                "14:45",
                "15:00",
                "15:15",
                "15:30",
                "15:45",
                "16:00",
                "16:15",
                "16:30",
                "16:45",
                "17:00"
        };
        model.addAttribute("times", times);

        String[] dates = VaccinationAppointmentController.dateCalculator();
        model.addAttribute("dates", dates);

        String[] centres = {
                "City West",
                "Aviva Stadium",
                "Lusk Tennis Club",
                "Walkinstown",
                "Mater Private",
                "Blanchardstown Hospital"
        };
        model.addAttribute("centres", centres);

        String[] brands = {
                "Pfizer",
                "Moderna"
        };
        model.addAttribute("brands", brands);

        VaccinationAppointment appointment = vaccinationAppointmentRepository.findById(appointmentId).get();
        model.addAttribute("appointment", appointment);

        return "hse/edit-appointment";
    }

    @PostMapping("/edit-appointment")
    public String editAppointments(@RequestParam Map<String, String> allParams, Model model) {
        BasicConfigurator.configure();
        logger.info("Appointment has been edited by Admin");
        String idAsString = allParams.get("appointment_id");
        int id = Integer.parseInt(idAsString);

        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        VaccinationAppointment appointmentToEdit = allAppointments.get(id);

        // Here we will edit the appointment using parameters and the setter
        Long longId = Long.valueOf(id);
        appointmentToEdit.setId(longId);
        appointmentToEdit.setBrandType(allParams.get("brand"));
        appointmentToEdit.setDoseNumber(Integer.parseInt(allParams.get("dose_number")));
        appointmentToEdit.setTimeSlot(allParams.get("time_slot"));
        appointmentToEdit.setVaccinationCentre(allParams.get("centre"));

        // deleting the current appointment value
        vaccinationAppointmentRepository.deleteById(longId);

        // Adding the updated version
        vaccinationAppointmentRepository.save(appointmentToEdit);

        // Updating the vaccination activity table
        Activity updatedActivity = new Activity();
        updatedActivity.setDate(java.time.LocalDate.now().toString());
        updatedActivity.setUser(appointmentToEdit.getUser());
        updatedActivity.setActivityType("Appointment edited");
        activityRepository.save(updatedActivity);

        model.addAttribute("editedAppointment", appointmentToEdit);
        return "hse/appointments";
    }

    @PostMapping("/administer")
    public String administer(@RequestParam Map<String, String> allParams, Model model) {
        // Update vaccine as administered & schedule new appointment
        BasicConfigurator.configure();
        logger.info("Vaccination has been administered");
        String idAsString = allParams.get("appointment_id");
        int id = Integer.parseInt(idAsString);

        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        VaccinationAppointment appointmentAdministered = allAppointments.get(id);

        // Increase current dose number by 1
        int doseNumber = appointmentAdministered.getDoseNumber();

        // automatically identifying a slot 3 weeks in the future if first dose
        if (doseNumber == 1) {
            VaccinationAppointment secondAppointment = new VaccinationAppointment();
            secondAppointment.setUser(appointmentAdministered.getUser());
            secondAppointment.setBrandType(appointmentAdministered.getBrandType());
            secondAppointment.setVaccinationCentre(appointmentAdministered.getVaccinationCentre());
            secondAppointment.setDoseNumber(doseNumber++);
            // calling method to calculate date of 3 weeks time (same slot)
            secondAppointment.setTimeSlot(threeWeeks(appointmentAdministered.getTimeSlot()));
            ;

            // adding 2nd appointment to DB
            vaccinationAppointmentRepository.save(secondAppointment);

            // Updating the vaccination activity table
            Activity updatedActivity = new Activity();
            updatedActivity.setDate(java.time.LocalDate.now().toString());
            updatedActivity.setUser(secondAppointment.getUser());
            updatedActivity.setActivityType("2nd appointment scheduled");
            activityRepository.save(updatedActivity);
        }

        Activity updatedActivity = new Activity();
        updatedActivity.setDate(java.time.LocalDate.now().toString());
        updatedActivity.setUser(appointmentAdministered.getUser());
        updatedActivity.setActivityType(doseNumber+"Vaccine administered");
        activityRepository.save(updatedActivity);


        model.addAttribute("vaxGiven", updatedActivity);
        return "redirect:/hse-admin/all-appointments";
    }

    // This method returns the given timestamp +21 days in the future
    public static String threeWeeks(String currentDate) {
        // going to be in this format
        // 12:00 2022-03-11
        String[] timeElements = currentDate.split(" ");

        String date = timeElements[1];

        String secondDoseDate = java.time.LocalDate.parse(date).plusDays(21).toString();
        String timeSlot = timeElements[0] + " " + secondDoseDate;
        
        return timeSlot;
    }

}