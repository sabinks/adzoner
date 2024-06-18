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
public class UserDetailDto {
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @NotEmpty(message = "Contact number cannot be empty.")
    private String contactNumber;
}
