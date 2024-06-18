package com.adzoner.api.service;

//import com.adzoner.api.dto.ReceiverDto;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.AddressException;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
//
//    @Autowired
//    JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.sender}")
//    private String mailSender;

//    public void sendMail(ReceiverDto receiverDto) throws MessagingException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        message.setFrom(new InternetAddress(mailSender));
//        message.setRecipients(MimeMessage.RecipientType.TO, receiverDto.getEmail());
//        message.setSubject(receiverDto.getSubject());
//        message.setContent(receiverDto.getBody(), "text/html; charset=utf-8");
//
//        javaMailSender.send(message);
//    }
}
