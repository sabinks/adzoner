package com.adzoner.api.controller.next;

import com.adzoner.api.dto.next.ContactUsDto;
import com.adzoner.api.service.next.NextService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/next")
public class NextContactUsController {
    @Autowired
    NextService nextService;
    @PostMapping("/contact-form-send-mail")
    ResponseEntity<String> contactFormSendMail(@Valid @RequestBody ContactUsDto contactUsDto) throws Exception {
        nextService.contactUsForm(contactUsDto);

        return new ResponseEntity<>("Message sent successfully, we will contact you soon!", HttpStatus.OK);
    }
}
