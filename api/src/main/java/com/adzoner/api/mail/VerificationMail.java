package com.adzoner.api.mail;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VerificationMail {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${spring.app.name}")
    private String appName;

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        receiverDto.setSubject("Email Verification");
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>Dear User,</h3>" +
                "<p>Please click link below to verify your email address.</p>" +
                "<a href='" + receiverDto.getUrl() + "'>Verify Email Address</a></br>" +
                "<p>Thank you,</p>" +
                "<p>" + appName + "</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        receiverDto.setBody(htmlContent);
        rabbitTemplate.convertAndSend("bargikrit_bigyapan_exchange",
                "bargikrit_bigyapan_routingkey", receiverDto);
    }
}
