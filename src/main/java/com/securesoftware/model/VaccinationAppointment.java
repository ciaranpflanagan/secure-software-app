package com.securesoftware.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "vaccinationappointments")
public class VaccinationAppointment {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private Long userId;
    @NotBlank
    private String timeSlot;
    @NotBlank
    private Integer doseNumber;
    @NotBlank
    private String vaccinationCentre;
    @NotBlank
    private String brandType;
    

    public VaccinationAppointment() {
        super();
    }

    public VaccinationAppointment(Long userID){
        super();
        this.userId = userID;
    }

    public VaccinationAppointment(Long id, Long userId, String timeSlot, Integer doseNumber, String vaccinationCentre, String brandType) {
        super();
        this.id = id;
        this.userId = userId;
        this.timeSlot = timeSlot;
        this.doseNumber = doseNumber;
        this.vaccinationCentre = vaccinationCentre;
        this.brandType = brandType;
    }



    public Long getUserId() {
        return this.userId;
    }


    public String getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getDoseNumber() {
        return this.doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public String getVaccinationCentre() {
        return this.vaccinationCentre;
    }

    public void setVaccinationCentre(String vaccinationCentre) {
        this.vaccinationCentre = vaccinationCentre;
    }

    public String getBrandType() {
        return this.brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }
}
