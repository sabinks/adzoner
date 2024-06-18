package com.adzoner.api.dto.advertisement;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryDto {
    @NotEmpty(message = "Category name is required.")
    private String name;
}
