package com.adzoner.api.dto.partner;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentSubmissionDto {
    @NotNull(message = "Please select document type.")
    @Min(value= 1, message = "Please select document type.")
    private Long documentTypeId;
}
