package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.model.VaccinationSlot;
import com.securesoftware.repository.VaccinationAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/vaccinationappointments")
public class VaccinationAppointmentController {

    private ArrayList<VaccinationSlot> availableSlots = new ArrayList<VaccinationSlot>();

    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;

    @GetMapping("/select-appointment")
    public String register() {

        availableSlots = setSlots();
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
                "17:00",
                "17:15",
                "17:30",
                "17:45",
                "18:00",
                "18:15",
                "18:30",
                "18:45",
                "19:00",
                "19:15",
                "19:30",
                "19:45"
        };

        String[] dates = dateCalculator();

        String[] vaccinationCentres = {
                "City West",
                "Aviva Stadium",
                "Dalkey Boat Club",
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
                    .plusDays(i)
                    .toString();
        }
        return dates;
    }

    // Small test to display how this is going to function


}