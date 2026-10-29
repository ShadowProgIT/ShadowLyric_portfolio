package com.example.demo.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "role", nullable = false)
    private String role;

    public Role() {

    }

    public Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}