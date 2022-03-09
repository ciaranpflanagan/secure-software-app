package com.securesoftware.controller;

import com.securesoftware.exception.UserNotFoundException;
import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.model.VaccinationSlot;
import com.securesoftware.repository.VaccinationAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/vaccinationappointments")
public class VaccinationAppointmentController {

    private ArrayList<VaccinationSlot> availableSlots = new ArrayList<VaccinationSlot>();

    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;

    /*
     * @RequestMapping("/selectAppointment")
     * public String appointmentSelection(Model model){
     * VaccinationAppointment vAppointment = new VaccinationAppointment();
     * }
     */

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

        String[] dates = {
                "00/03/2021",
                "01/03/2021",
                "02/03/2021",
                "03/03/2021",
                "04/03/2021",
                "05/03/2021",
                "06/03/2021",
                "07/03/2021",
                "08/03/2021",
                "09/03/2021",
                "10/03/2021",
                "11/03/2021",
                "12/03/2021",
                "13/03/2021",
                "14/03/2021",
                "15/03/2021",
                "16/03/2021",
                "17/03/2021",
                "18/03/2021",
                "19/03/2021",
                "20/03/2021",
                "21/03/2021",
                "22/03/2021",
                "23/03/2021",
                "24/03/2021",
                "25/03/2021",
                "26/03/2021",
                "27/03/2021",
                "28/03/2021",
                "29/03/2021",
                "30/03/2021",
                "31/03/2021"
        };

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

        for (int i=0;i < dates.length; i++){
            for (int j = 0;j <times.length;j++){
                String tempTime = times[j]+" "+dates[i];
                formattedTimes.add(tempTime);
            }
        }

      // For loop to assign a slot to each hospital
      for (int i = 0;i < vaccinationCentres.length; i++)  {
          if (i%2 == 0){
              for (String time : formattedTimes)
              allSlots.add(new VaccinationSlot(vaxBrands[0], time, vaccinationCentres[i]));
          }

          if (i%2 == 1){
            for (String time : formattedTimes)
            allSlots.add(new VaccinationSlot(vaxBrands[1], time, vaccinationCentres[i]));
        }
      }

      return allSlots;

    }

    // Small test to display how this is going to function
    public static void main(String[] args) {
        System.out.println("Length "+setSlots().size()+"\nFirstElement "+setSlots().get(0).getTimeSlot()+" "+setSlots().get(0).getVaccinationCentre()+" "+setSlots().get(0).getBrandType());
        
    }

}
