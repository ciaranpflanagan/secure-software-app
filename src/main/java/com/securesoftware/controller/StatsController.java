package com.securesoftware.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.securesoftware.model.User;
import com.securesoftware.model.VaccinationAppointment;
import com.securesoftware.repository.UserRepository;
import com.securesoftware.repository.VaccinationAppointmentRepository;
import com.securesoftware.app.AES;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.log4j.BasicConfigurator;  
import org.apache.log4j.LogManager;  
import org.apache.log4j.Logger; 


@Controller
@RequestMapping(path = "/stats")
public class StatsController {
    /**
     * Displays the forum
     */

    /*
     * Any user should be able to visualize aggregated statistics about the
     * vaccination campaign, such as vaccinated age groups, male VS female,
     * nationalities.
     */

    @Autowired
    UserRepository userRepository;

    @Autowired
    VaccinationAppointmentRepository vaccinationAppointmentRepository;

    private static final Logger logger = LogManager.getLogger(StatsController.class);

    @GetMapping("/all")
    public String showForum(Model model) {
        // Age demographics
        // Nationality demographics
        // Vaccination brands used
        // ###################################################################################
        BasicConfigurator.configure();
        logger.info("The statistics page has been viewed");
        List<User> allUsers = userRepository.findAll();
        final String sk = "eochair";

        ArrayList<String> years = new ArrayList<String>();

        ArrayList<String> nationalities = new ArrayList<String>();

        // getting all years of users
        for (User user : allUsers) {
            String dob = AES.decrypt(user.getDOB(), sk);
            String year = dob.substring(dob.length() - 4);
            years.add(year);

            String nationality = user.getNationality().toLowerCase();
            nationalities.add(nationality);
        }

        Map<String, Integer> yearFreqMap = countFrequencies(years);
        Map<String, Integer> nationalityFreqMap = countFrequencies(nationalities);


        // Vaccination brand types
        List<VaccinationAppointment> existingAppointments = vaccinationAppointmentRepository.findAll();

        ArrayList<String> brands = new ArrayList<String>();

        for (VaccinationAppointment vaccinationAppointment : existingAppointments){
            String vaxBrand = vaccinationAppointment.getBrandType();
            brands.add(vaxBrand);
        }

        Map<String, Integer> brandFreqMap = countFrequencies(brands);

        model.addAttribute("yearFreqMap", yearFreqMap);
        model.addAttribute("nationalityFreqMap", nationalityFreqMap);
        model.addAttribute("brandFreqMap", brandFreqMap);

        return "stats/aggregate-stats";
    }

    public static Map<String, Integer> countFrequencies(ArrayList<String> list) {
        // hashmap to store the frequency of element
        Map<String, Integer> yearFreqMap = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = yearFreqMap.get(i);
            yearFreqMap.put(i, (j == null) ? 1 : j + 1);
        }

        return yearFreqMap;
    }

}
