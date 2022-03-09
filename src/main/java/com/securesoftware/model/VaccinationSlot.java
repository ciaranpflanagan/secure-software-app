package com.securesoftware.model;

public class VaccinationSlot {

    /* This class represents a uniform method
    *  for working with available slots.
    *  We can use this to generate the available slots
    *  and we can identify occupied slots by taking away those in the VaccinationAppointment table.
    */

    private String brandType;

    private String timeSlot;

    private String vaccinationCentre;



    public VaccinationSlot(String brandType, String timeSlot, String vaccinationCentre) {
        this.brandType = brandType;
        this.timeSlot = timeSlot;
        this.vaccinationCentre = vaccinationCentre;
    }

    public String getBrandType() {
        return this.brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getVaccinationCentre() {
        return this.vaccinationCentre;
    }

    public void setVaccinationCentre(String vaccinationCentre) {
        this.vaccinationCentre = vaccinationCentre;
    }

    
}
