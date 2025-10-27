package com.dms.dto;

import com.dms.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    private String id;

    @NotBlank(message="email is required", groups = {ValidationGroups.Login.class, ValidationGroups.register.class})
    @Email(message = "email is not valid", groups = {ValidationGroups.Login.class, ValidationGroups.register.class})
    private String email;

    @NotBlank(message = "password is required",  groups = {ValidationGroups.Login.class, ValidationGroups.register.class})
    private String password;

    @NotBlank(message = "national id is required", groups = ValidationGroups.register.class)
    private String nationalId;

    @NotBlank(message = "first name is required", groups = ValidationGroups.register.class)
    private String firstName;

    @NotBlank(message = "last name is required", groups = ValidationGroups.register.class)
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

