package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class ShowInfoUserDto {

    private String username;
    private String email;
    private List<RoleDto> roles = new ArrayList<>();


    public String getUsername() {
            return username;
        }

    public void setUsername(String username) {
            this.username = username;
        }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public String getLastRole() {
        if (roles == null || roles.isEmpty()) {
            return "-";
        }
        return roles.get(roles.size() - 1).getRole();
    }
}
