package com.adzoner.api.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterDto {
    @NotEmpty(message = "Email address is required!")
//    @Pattern(regexp = "/[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,4}/" , message = "Please provide valid email!")
    @Email(message = "Please provide valid email!")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Phone number is required!")
    private String contactNumber;

    @NotEmpty(message = "Name is required!")
    private String name;

    @NotEmpty(message = "Password is required!")
//    @Min(value = 8, message = "Password must have at least 8 character!")
    private String password;


}
