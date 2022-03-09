package com.securesoftware.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vaccination_appointment_id", referencedColumnName = "id")
    private VaccinationAppointment vaccinationAppointment;

    @NotBlank
    private String activityType;


    public Activity() {
        super();
    }


    public Activity(Long id, User user, VaccinationAppointment vaccinationAppointment, String activityType) {
        super();
        this.id = id;
        this.user = user;
        this.vaccinationAppointment = vaccinationAppointment;
        this.activityType = activityType;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VaccinationAppointment getVaccinationAppointment() {
        return this.vaccinationAppointment;
    }

    public void setVaccinationAppointment(VaccinationAppointment vaccinationAppointment) {
        this.vaccinationAppointment = vaccinationAppointment;
    }

    public String getActivityType() {
        return this.activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

}
