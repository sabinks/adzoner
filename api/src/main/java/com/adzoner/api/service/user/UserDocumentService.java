package com.adzoner.api.service.user;

import com.adzoner.api.dto.ReceiverDto;
import com.adzoner.api.dto.partner.DocumentSubmissionDto;
import com.adzoner.api.entity.DocumentType;
import com.adzoner.api.entity.user.Document;
import com.adzoner.api.entity.User;
import com.adzoner.api.mail.admin.UserUploadedDocumentMail;
import com.adzoner.api.repository.DocumentTypeRepository;
import com.adzoner.api.repository.UserRepository;
import com.adzoner.api.repository.user.UserDocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.adzoner.api.utility.MyUtilityClass.getExtensionFromFilename;

@Service
public class UserDocumentService {
    @Autowired
    UserDocumentRepository userDocumentRepository;

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ServletContext servletContext;
    @Autowired
    UserUploadedDocumentMail userUploadedDocumentMail;

    @Value("${spring.upload.directory}")
    private String uploadDirectory;

    @Value("${spring.admin.email}")
    private String adminEmail;

    @Value("${spring.app.name}")
    private String appName;

    @Transactional
    public void documentSubmission(DocumentSubmissionDto documentSubmissionDto, MultipartFile[] images) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User record not found!");
        }
//        if(!user.get(0).getCanPublish()){
//            throw new Exception("Partner cannot publish advertisements, please contact admin!");
//        }
        Optional<DocumentType> documentType = documentTypeRepository.findById(documentSubmissionDto.getDocumentTypeId());
        if (documentType.isEmpty()) {
            throw new EntityNotFoundException("Document type not found!");
        }
        if (images.length == 0) {
            throw new Exception("No images uploaded!");
        }

        for (MultipartFile image : images) {
            if (image != null) {
                Document document = new Document();
                System.out.println( image.getOriginalFilename());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String original_document_name = image.getOriginalFilename();
                Optional<String> extension = getExtensionFromFilename(original_document_name);
                if (extension.isEmpty()) {
                    throw new Exception("File does not have extension!");
                }
                String document_name = timestamp.getTime() + "." + extension.get();

                Path filenameAndPath = Paths.get(uploadDirectory + "/documents", document_name);
                document.setDocumentType(documentType.get());
                document.setDocumentName(document_name);
                document.setOriginalDocumentName(original_document_name);
                document.setUser(user.get(0));
                Files.write(filenameAndPath, image.getBytes());
                userDocumentRepository.save(document);
            }
        }
        Map<String, String> data = new HashMap<>();
        data.put("userName", user.get(0).getName());
        data.put("userEmail", user.get(0).getEmail());
        ReceiverDto receiverDto = new ReceiverDto();
        receiverDto.setName(appName);
        receiverDto.setEmail(adminEmail);
        receiverDto.setData(data);

        userUploadedDocumentMail.sendMail(receiverDto);
    }

    public List<Document> userDocuments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        return userDocumentRepository.findByUserId(user.get(0), PageRequest.of(0, 1000, Sort.Direction.DESC, "id"));
    }

    public List<Document> userDocumentsById(Long id) {
        List<Document> documents = userDocumentRepository.findByUserId(id);
        if (documents.isEmpty()) {
            throw new EntityNotFoundException("User document not found!");
        }
        return documents;
    }


}
