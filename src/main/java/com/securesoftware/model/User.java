package com.securesoftware.model;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String first_name;
    @NotBlank
    private String last_name;
    @NotBlank
    private String dob;
    @NotBlank
    private String pps;
    @NotBlank
    private String address;
    @NotBlank
    private String phone_number;
    @NotBlank
    private String email;
    @NotBlank
    private String nationality;
    @NotBlank
    private String password;
    @Column(name = "account_locked")
    private boolean accountLocked;
    @Column(name = "attempts")
    private int attempts;

    @ManyToMany 
    @JoinTable( 
        name = "user_role", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private Set<VaccinationAppointment> vaccinationAppointments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private Set<Activity> activities;

    public User() {
        super();
    }

    public User(
        Long id,
        String first_name,
        String last_name,
        String dob,
        String pps,
        String address,
        String phone_number,
        String email,
        String nationality,
        String password,
        Set<Role> roles
    ) {
        super();
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.pps = pps;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.nationality = nationality;
        this.password = password; // This will need to be hashed
        this.roles = roles;
    }

    /**
     *********************
     *      Getters
     *********************
     */
    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return first_name;
    }
    public String getLastName() {
        return last_name;
    }
    public String getDOB() {
        return dob;
    }
    public String getPPSNumber() {
        return pps;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phone_number;
    }
    public String getEmail() {
        return email;
    }
    public String getNationality() {
        return nationality;
    }
    public String getPassword() {
        return password;
    }
    public boolean getIsAccountLocked() {
        return accountLocked;
    }
    public int getAttempts() {
        return attempts;
    }
    public Set<Role> getRole() {
        return roles;
    }
    public Set<VaccinationAppointment> getVaccinationAppointments() {
        return this.vaccinationAppointments;
    }
    public Set<Activity> getActivities() {
        return this.activities;
    }

    /**
     *********************
     *      Setters
     *********************
     */
    public void setId(Long id) {
        this.id = id;
    }
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }
    public void setDOB(String dob) {
        this.dob = dob;
    }
    public void setPPSNumber(String pps) {
        this.pps = pps;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAttempts(Integer num) {
        this.attempts = num;
    }
    public void setAccountLocked(boolean val) {
        this.accountLocked = val;
    }
    public void setRole(Set<Role> roles) {
        this.roles = roles;
    }
    public void setVaccinationAppointments(Set<VaccinationAppointment> vaccinationAppointments) {
        this.vaccinationAppointments = vaccinationAppointments;
    }
    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

}