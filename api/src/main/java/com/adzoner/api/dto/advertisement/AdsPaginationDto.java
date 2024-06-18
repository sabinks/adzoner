package com.adzoner.api.dto.advertisement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdsPaginationDto {
    private Long id;
    private String productName;
    private String productDescription;
    private String barCode;
    private String productCode;
}
