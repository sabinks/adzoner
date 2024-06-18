package com.adzoner.api.service;

import com.adzoner.api.entity.DocumentType;
import com.adzoner.api.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {
    @Autowired
    DocumentTypeRepository documentTypeRepository;

    public List<DocumentType> getDocumentTypes() {
        return documentTypeRepository.findAll();
    }
}
