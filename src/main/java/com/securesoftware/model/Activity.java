package com.securesoftware.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank
    private String activityType;

    @NotBlank
    private String date;


    public Activity() {
        super();
    }


    public Activity(Long id, User user, String activityType, String date) {
        super();
        this.id = id;
        this.user = user;
        this.activityType = activityType;
        this.date = date;
    }

    /**
     *********************
     *      Getters
     *********************
     */
    public Long getId() {
        return this.id;
    }
    public User getUser() {
        return this.user;
    }
    public String getActivityType() {
        return this.activityType;
    }
    public String getDate() {
        return this.date;
    }

    /**
     *********************
     *      Setters
     *********************
     */
    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
