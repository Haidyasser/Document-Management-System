package com.dms.dto;

import com.dms.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;


@Getter
@Setter

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
}
