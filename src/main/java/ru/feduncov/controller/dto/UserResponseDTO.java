package ru.feduncov.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.feduncov.entity.User;

import java.time.LocalDateTime;

public class UserResponseDTO {

    @JsonProperty("id")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("e_mail")
    private String email;

    @JsonProperty("reg_time")
    private LocalDateTime regTime;


    @JsonProperty("role")
    private String role;

    public UserResponseDTO() {
    }

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.regTime = user.getRegTime();
        this.role = user.getRole().getName();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
