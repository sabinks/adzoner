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
public class ForgotPasswordDto {
    @NotEmpty
    private String email;
}
