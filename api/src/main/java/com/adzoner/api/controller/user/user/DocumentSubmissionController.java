package com.adzoner.api.controller.user.user;


import com.adzoner.api.dto.partner.DocumentSubmissionDto;
import com.adzoner.api.service.user.UserDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class DocumentSubmissionController {
    @Autowired
    UserDocumentService userDocumentService;
    @PreAuthorize("hasAnyRole('PARTNER', 'USER')")
    @PostMapping(value = "/documents-submission", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> documentsSubmission(@Valid @ModelAttribute DocumentSubmissionDto documentSubmissionDto
        ,@RequestParam("images") MultipartFile[] images) throws Exception {
        userDocumentService.documentSubmission(documentSubmissionDto, images);
        return new ResponseEntity<>("Document uploaded!", HttpStatus.OK);
    }
}
