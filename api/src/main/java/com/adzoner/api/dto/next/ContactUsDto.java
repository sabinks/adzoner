package com.adzoner.api.dto.next;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@Getter
@Setter
@Service
public class ContactUsDto {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Phone number is required!")
    private String email;
    @NotEmpty(message = "Phone number is required!")
    private String phone;
    @NotEmpty(message = "Subject is required!")
    private String subject;

    @NotEmpty(message = "Message body is required!")
    private String message;

}
