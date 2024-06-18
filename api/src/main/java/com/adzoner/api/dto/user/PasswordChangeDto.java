package com.adzoner.api.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@Getter
@Setter
@Service
public class PasswordChangeDto {
    @NotEmpty(message = "Current password is required.")
    private String currentPassword;

    @NotEmpty(message ="New password is required.")
    private String password;

    @NotEmpty(message = "Confirm password is required.")
    private String passwordConfirmation;
}
