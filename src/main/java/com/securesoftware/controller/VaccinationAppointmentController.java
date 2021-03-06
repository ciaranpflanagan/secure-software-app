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
@RequestMapping(path = "/vaccinationappointments")
public class VaccinationAppointmentController {

    private ArrayList<VaccinationSlot> allSlots = new ArrayList<VaccinationSlot>();

    private ArrayList<VaccinationSlot> availableSlots = new ArrayList<VaccinationSlot>();

    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    private static final Logger logger = LogManager.getLogger(VaccinationAppointmentController.class);  

    @GetMapping("/select-appointment")
    public String register(Model model) {
        // Prepopulating available slots
        allSlots = setSlots();

        // Below removes slots which are already taken
        ArrayList<VaccinationSlot> takenSlots = new ArrayList<>();

        List<VaccinationAppointment> existingAppointments = vaccinationAppointmentRepository.findAll();

        for (VaccinationAppointment existingAppointment : existingAppointments) {
            takenSlots.add(new VaccinationSlot(existingAppointment.getBrandType(), existingAppointment.getTimeSlot(),
                    existingAppointment.getVaccinationCentre()));
        }

        // All available slots
        availableSlots = allSlots;
        availableSlots.removeAll(takenSlots);

        // Checking if a user has not already registered
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        // If user already has an appointment
        User user = userRepository.findByEmail(email);
        boolean isRegistered = false;
        int appointmentsMade = 0;
        ArrayList<VaccinationAppointment> usersAppointments = new ArrayList<>();

        for (VaccinationAppointment existingAppointment : existingAppointments) {
            if (existingAppointment.getUser().getId() == user.getId()) {
                isRegistered = true;
                usersAppointments.add(existingAppointment);
                appointmentsMade++;
            }
        }

        // checking if the user requires a second dose
        if (isRegistered) {
            model.addAttribute("appointmentNeeded", false);

            // return date of most recent appointment
            model.addAttribute("nextAppointment",
                    (appointmentsMade == 1) ? usersAppointments.get(0) : usersAppointments.get(1));
        } else {
            // If the user hasn't an appointment already
            model.addAttribute("appointmentNeeded", true);
            model.addAttribute("slots", availableSlots);
        }

        // if user has 0 appointments or vaccines they can choose a slot from below
        // Should lead the user to the page to choose relevant slots
        // Prepopulating available slots

        return "vaccinationAppointments/VaccineSelection";
    }

    @PostMapping("/save-appointment")
    public String save(@RequestParam Map<String, String> allParams, Model model) {
        BasicConfigurator.configure();
        logger.info("New appointment has been booked");
        // Save the data
        String chosenSlot = allParams.get("appointment");

        // chosenSlot in format "Pfizer/12:00 2022-03-11/City West"
        String[] arrOfStr = chosenSlot.split("/");

        String brandType = arrOfStr[0];
        String timeSlot = arrOfStr[1];
        String vaccinationCentre = arrOfStr[2];

        // Getting user details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        User user = userRepository.findByEmail(email);

        VaccinationAppointment newAppointment = new VaccinationAppointment();
        newAppointment.setUser(user);
        newAppointment.setTimeSlot(timeSlot);
        newAppointment.setBrandType(brandType);
        newAppointment.setDoseNumber(1);
        newAppointment.setVaccinationCentre(vaccinationCentre);

        vaccinationAppointmentRepository.save(newAppointment);
        model.addAttribute("appointment", newAppointment);

        // Updating the vaccination activity table
        Activity updatedActivity = new Activity();
        updatedActivity.setDate(newAppointment.getTimeSlot());
        updatedActivity.setUser(newAppointment.getUser());
        updatedActivity.setActivityType("Vaccine apppointment made");
        activityRepository.save(updatedActivity);
        // Will we need some frontend magic here?
        return "vaccinationAppointments/VaccineSelection";
    }

    @PostMapping("/remove-appointment")
    public String remove(@RequestParam Map<String, String> allParams) {
        BasicConfigurator.configure();
        logger.info("Appointment has been cancelled");
        // Save the data
        String doseNumberAString = allParams.get("does_number");
        int doseNumber = Integer.parseInt(doseNumberAString);

        // Getting user details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        User user = userRepository.findByEmail(email);

        // Removes the cancelled appointment from the DB
        List<VaccinationAppointment> allAppointments = vaccinationAppointmentRepository.findAll();

        for (VaccinationAppointment appointment : allAppointments) {
            if (appointment.getUser() == user && appointment.getDoseNumber() == doseNumber) {
                vaccinationAppointmentRepository.deleteById(appointment.getId());
                
                // Updating the vaccination activity table
                Activity updatedActivity = new Activity();
                updatedActivity.setDate(appointment.getTimeSlot());
                updatedActivity.setUser(appointment.getUser());
                updatedActivity.setActivityType("Vaccination appointment cancelled");
                activityRepository.save(updatedActivity);
            }
        }

        return "vaccinationAppointments/VaccineSelection";
    }

    public static ArrayList<VaccinationSlot> setSlots() {
        ArrayList<VaccinationSlot> allSlots = new ArrayList<VaccinationSlot>();

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

        String[] dates = dateCalculator();

        String[] vaccinationCentres = {
                "City West",
                "Aviva Stadium",
                "Lusk Tennis Club",
                "Walkinstown",
                "Mater Private",
                "Blanchardstown Hospital"
        };

        String[] vaxBrands = {
                "Pfizer",
                "Moderna"
        };

        // For loop to generate times

        ArrayList<String> formattedTimes = new ArrayList<>();

        for (int i = 0; i < dates.length; i++) {
            for (int j = 0; j < times.length; j++) {
                String tempTime = times[j] + " " + dates[i];
                formattedTimes.add(tempTime);
            }
        }

        // For loop to assign a slot to each hospital
        for (int i = 0; i < vaccinationCentres.length; i++) {
            if (i % 2 == 0) {
                for (String time : formattedTimes)
                    allSlots.add(new VaccinationSlot(vaxBrands[0], time, vaccinationCentres[i]));
            }

            if (i % 2 == 1) {
                for (String time : formattedTimes)
                    allSlots.add(new VaccinationSlot(vaxBrands[1], time, vaccinationCentres[i]));
            }
        }

        return allSlots;

    }

    // Returns a current list of dates from today to 30 days from now
    public static String[] dateCalculator() {
        String currentDate = java.time.LocalDate.now().toString();
        String[] dates = new String[31];
        for (int i = 0; i < 31; i++) {
            dates[i] = java.time.LocalDate
                    .parse(currentDate)
                    .plusDays(i + 1)
                    .toString();
        }
        return dates;
    }

}
