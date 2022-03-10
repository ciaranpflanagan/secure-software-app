package com.securesoftware.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "forum_posts")
public class ForumPost {
    @Id
    @GeneratedValue
    private Long id;
    private Long user_id;
    
    private Long parent_id; // Parent post id

    @NotBlank
    private String title;
    @NotBlank
    private String body;
    @Column(name = "posted_at", nullable = false, updatable = false)
    private Date posted_at;

    public ForumPost() {
        super();
    }

    public ForumPost(Long id, Long user_id, Long parent_id, String title, String body, Date posted_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.parent_id = parent_id;
        this.title = title;
        this.body = body;
        this.posted_at = posted_at;
    }

    /**
     *********************
     *      Getters
     *********************
     */
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return user_id;
    }
    public Long getParentId() {
        return parent_id;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public Date getPostedAt() {
        return posted_at;
    }


    /**
     *********************
     *      Setters
     *********************
     */
    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }
    public void setParentId(Long parent_id) {
        this.parent_id = parent_id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setPostedAt(Date posted_at) {
        this.posted_at = posted_at;
    }
}
