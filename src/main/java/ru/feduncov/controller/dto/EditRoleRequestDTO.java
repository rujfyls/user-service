package ru.feduncov.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditRoleRequestDTO {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("role")
    private String role;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
