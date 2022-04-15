package ru.feduncov.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class UserRequestDTO implements Serializable {
    @JsonProperty("name")
    @NotBlank(message = "Необходимо указать имя")
    private String name;

    @JsonProperty("e_mail")
    @Pattern(regexp = "([A-z0-9._]+@[A-z]+\\.[a-z]{2,3})", message = "Не правильный формат email")
    private String email;


    @JsonProperty("password")
    @NotBlank(message = "Необходимо указать пароль")
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
