package com.securesoftware.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/stats")
public class StatsController {
    /**
     * Displays the forum
     */
    @GetMapping("/all")
    public String showForum(Model model) {
        // List<Appoin> posts = forumRepository.findAll();
        // model.addAttribute("posts", posts);

        return "stats/aggregate-stats";
    }
}
