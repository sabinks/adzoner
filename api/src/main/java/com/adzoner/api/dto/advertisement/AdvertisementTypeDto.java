package com.adzoner.api.dto.advertisement;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdvertisementTypeDto {
    @NotEmpty(message = "Advertisement type name is required.")
    private String name;
}
