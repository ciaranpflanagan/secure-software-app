package com.securesoftware.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.securesoftware.model.ForumPost;
import com.securesoftware.model.User;
import com.securesoftware.repository.ForumRepository;
import com.securesoftware.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/forum")
public class ForumController {
    @Autowired
    UserRepository UserRepository;
    @Autowired
    ForumRepository forumRepository;

    /**
     * Displays the forum
     */
    @GetMapping("/posts")
    public String showForum(Model model) {
        List<ForumPost> posts = forumRepository.findAll();
        model.addAttribute("posts", posts);

        return "forum/posts";
    }

    /**
     * Displays the page used to reply to a forum post
     */
    @GetMapping("/reply/{id}")
    public String showReplyPage(Model model, @PathVariable(value = "id") Long postId, @RequestParam Map<String,String> allParams) {
        // Get single forum post and return it to view
        ForumPost post = forumRepository.findById(postId).get();
        model.addAttribute("post", post);

        return "forum/reply";
    }

    /**
     * Displays the page used to show the post answer
     */
    @GetMapping("/answer/{id}")
    public String showAnswerPage(Model model, @PathVariable(value = "id") Long postId, @RequestParam Map<String,String> allParams) {
        ForumPost question = forumRepository.findById(postId).get();
        model.addAttribute("question", question);
        
        ForumPost answer = forumRepository.findByParentId(postId);
        model.addAttribute("answer", answer);

        return "forum/answer";
    }

    /**
     * Displays the page used to post a new forum post
     */
    @GetMapping("/new")
    public String showNewPost(@RequestParam Map<String,String> allParams) {
        return "forum/new";
    }

    /**
     * Saves the forum posts
     * @param allParams
     * @return
     */
    @PostMapping("/save")
    public String saveForumPost(@RequestParam Map<String,String> allParams) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails)principal).getUsername();

        User user = UserRepository.findByEmail(email);

        // Saving forum post
        ForumPost post = new ForumPost();
        post.setUserId(user.getId());
        post.setTitle(allParams.get("title"));
        post.setBody(allParams.get("body"));
        post.setPostedAt(new Date());
        
        forumRepository.save(post);

        return "forum/posts"; // Return to forum page
    }

    /**
     * Saves the forum posts
     * @param allParams
     * @return
     */
    @PostMapping("/save-reply")
    public String saveForumPostReply(@RequestParam Map<String,String> allParams) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails)principal).getUsername();

        User user = UserRepository.findByEmail(email);

        // Saving forum post
        ForumPost post = new ForumPost();
        post.setUserId(user.getId());
        post.setParentId(Long.parseLong(allParams.get("post"))); // Id of the parent post
        post.setTitle(allParams.get("title"));
        post.setBody(allParams.get("body"));
        post.setPostedAt(new Date());
        
        forumRepository.save(post);

        return "forum/posts"; // Return to forum page
    }
}
