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

    @GetMapping("/all-appointments")
    public String viewAppointments(Model model) {
        // Add preventative measures for non-admins
        /*
        ****************************************************
        */
        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        model.addAttribute("allAppointments", allAppointments);

        return "hse/appointments";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(value = "id") Long appointmentId) {
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
        String idAsString = allParams.get("appointment_id");
        int id = Integer.parseInt(idAsString);

        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();
        VaccinationAppointment appointmentAdministered = allAppointments.get(id);

        int doseNumber = appointmentAdministered.getDoseNumber();

        // Increase current dose number by 1
        appointmentAdministered.setDoseNumber(doseNumber++);

        Long longId = Long.valueOf(id);
        appointmentAdministered.setId(longId);
        // deleting the current appointment value
        vaccinationAppointmentRepository.deleteById(longId);

        // Adding the updated version
        vaccinationAppointmentRepository.save(appointmentAdministered);

        model.addAttribute("administeredVaccine", appointmentAdministered);
        return "redirect:/hse-admin/all-appointments";
    }

}