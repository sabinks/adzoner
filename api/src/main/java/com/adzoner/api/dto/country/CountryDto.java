package com.adzoner.api.dto.country;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CountryDto {
    @NotEmpty(message = "Name is required.")
    @Column(length = 50)
    private String name;
}
