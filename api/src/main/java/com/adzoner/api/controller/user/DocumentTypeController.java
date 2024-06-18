package com.adzoner.api.controller.user;

import com.adzoner.api.entity.DocumentType;
import com.adzoner.api.repository.DocumentTypeRepository;
import com.adzoner.api.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentTypeController {
    @Autowired
    DocumentTypeService documentTypeService;
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @GetMapping("/document-types")
    public List<DocumentType> getDocumentTypes(){
        return documentTypeService.getDocumentTypes();
    }
}
