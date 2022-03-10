package com.securesoftware.controller;

import java.util.Date;
import java.util.Map;

import com.securesoftware.model.ForumPost;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/forum")
public class ForumController {

    /**
     * Displays the forum
     */
    @GetMapping("/posts")
    public String showForum() {
        return "forum/posts";
    }

    @PostMapping("/save")
    public String saveForumPost(@RequestParam Map<String,String> allParams) {
        // Make forum post object here and save
        ForumPost post = new ForumPost();
        post.setUserId(Long.parseLong(allParams.get("user_id")));
        post.setTitle(allParams.get("title"));
        post.setBody(allParams.get("body"));
        post.setPostedAt((java.sql.Date) new Date());

        return "forum/posts"; // Return to forum page
    }
}
