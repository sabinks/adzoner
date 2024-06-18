package com.adzoner.api.controller.user.user;

import com.adzoner.api.entity.user.Document;
import com.adzoner.api.entity.User;
import com.adzoner.api.repository.UserRepository;
import com.adzoner.api.repository.user.UserDocumentRepository;
import com.adzoner.api.service.user.UserDocumentService;
import com.adzoner.api.utility.MyUtilityClass;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class UserDocumentController {
    @Value("${spring.upload.directory}")
    private String uploadDirectory;

    @Autowired
    UserDocumentRepository userDocumentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDocumentService userDocumentService;

    @PreAuthorize("hasAnyRole('PARTNER', 'USER')")
    @GetMapping("member-documents")
    ResponseEntity<List<Document>> userDocuments() {
        List<Document> documents = userDocumentService.userDocuments();

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','PARTNER', 'USER')")
    @DeleteMapping("member-documents/{documentName}")
    ResponseEntity<String> userDocuments(@PathVariable String documentName) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        Optional<Document> document =  userDocumentRepository.findByDocumentName(documentName);
        if(document.isEmpty()){
            throw new EntityNotFoundException("No document found!");
        }
        userDocumentRepository.delete(document.get());
        String filePath = uploadDirectory + "/documents/" + documentName;
        Files.delete(Paths.get(filePath));

        return new ResponseEntity<>("Document deleted!", HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    @GetMapping("member-documents/{id}")
    ResponseEntity<List<Document>> userDocumentsById(@PathVariable Long id) {
       List<Document> documents =  userDocumentService.userDocumentsById(id);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','PARTNER', 'USER')")
    @GetMapping("member-document/{filename}")
    ResponseEntity<?> getImage(@PathVariable String filename) throws IOException {
        Optional<Document> document = userDocumentRepository.findByDocumentName(filename);
        if (document.isEmpty()) {
            throw new EntityNotFoundException("Document not found");
        }
        String filePath = uploadDirectory + "/documents/" + filename;
        String extension = String.valueOf(MyUtilityClass.getExtensionFromFilename(filename));
        try {
            File file = new File(filePath);
            byte[] image = Files.readAllBytes(file.toPath());
            return new ResponseEntity<>(Base64.encodeBase64String(image), HttpStatus.ACCEPTED);

        } catch (NoSuchFileException ignored) {
            System.out.println("File not round!");
            return new ResponseEntity<>("File not found", HttpStatus.CONFLICT);
        }
    }
}



