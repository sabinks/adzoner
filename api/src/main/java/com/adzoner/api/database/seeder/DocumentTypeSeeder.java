package com.adzoner.api.database.seeder;

import com.adzoner.api.entity.DocumentType;
import com.adzoner.api.repository.DocumentTypeRepository;
import com.adzoner.api.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeSeeder implements CommandLineRunner {
    @Autowired
    DocumentTypeRepository documentTypeRepository;

    public void run(String... args) throws Exception {
//        seedDocumentTypeData();
    }

    private void seedDocumentTypeData() {
        if (documentTypeRepository.count() == 0) {
            DocumentType documentType1 = new DocumentType();
            documentType1.setName("Citizenship");
            documentTypeRepository.save(documentType1);

            DocumentType documentType2 = new DocumentType();
            documentType2.setName("Passport");
            documentTypeRepository.save(documentType2);

            DocumentType documentType3 = new DocumentType();
            documentType3.setName("Driving License");
            documentTypeRepository.save(documentType3);

            DocumentType documentType4 = new DocumentType();
            documentType4.setName("Company Certificate");
            documentTypeRepository.save(documentType4);
        }
//        System.out.println("Total DocumentTypes: " + documentTypeRepository.count());
    }
}
