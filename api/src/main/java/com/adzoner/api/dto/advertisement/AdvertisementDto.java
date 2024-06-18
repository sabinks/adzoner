package com.adzoner.api.dto.advertisement;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AdvertisementDto {

    @NotEmpty(message = "Company name is required.")
    @Column(length = 50)
    private String companyName;

    @NotEmpty(message = "Name is required.")
    @Column(length = 200)
    private String name;

    @NotEmpty(message = "Advertisement content is required.")
    private String data;

    @NotNull(message="Atleast one category is required.")
    private String selectedCategoryIds;

    @NotNull(message = "Country name is required.")
    @Min(value = 1, message = "Country name is required.")
    private Long countryId;

    @NotNull(message = "Province name is required.")
    @Min(value =1, message = "Province name is required.")
    private Long provinceId;

//    @NotNull(message = "District name is required.")
//    @Min(value =1, message = "District name is required.")
    private Long districtId;

    @NotEmpty(message = "Email is required.")
    @Column(length = 255)
    private String email;

    @NotEmpty(message = "Phone is required.")
    @Column(length = 15)
    private String contactNumber = "";
    private String imageRemoveIds;
    private String website = "";
    private Boolean showEmail;
    private Boolean showContactNumber;
    private Boolean showWebsite;
}
