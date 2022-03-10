package com.securesoftware.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue()
    private Long id;
    @NotBlank
    private String role;

    public Role() {
        super();
    }

    public Role(Long id, String role) {
        super();
        this.id = id;
        this.role = role;
    }

    /**
     *********************
     *      Getters
     *********************
     */
    public Long getId() {
        return id;
    }
    public String getRole() {
        return role;
    }

    /**
     *********************
     *      Setters
     *********************
     */
    public void setId(Long id) {
        this.id = id;
    }
    public void setRole(String role) {
        this.role = role;
    }
}