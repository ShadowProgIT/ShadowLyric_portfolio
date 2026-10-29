package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShowDetailedInfoUserDto {
    private String username;
    private List<RoleDto> roles = new ArrayList<>();
    private String email;
    private String phone;
    private BigDecimal wallet;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public String getLastRole() {
        if (roles == null || roles.isEmpty()) {
            return "-";
        }
        return roles.get(roles.size() - 1).getRole();
    }
}