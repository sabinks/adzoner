package com.adzoner.api.mail.admin;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserUploadedDocumentMail {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.app.name}")
    private String appName;

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        receiverDto.setSubject("Attention document submission required");
        Map<String, String> data = receiverDto.getData();
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>" + "Dear " + receiverDto.getName() + ",</h3>" +
                "<p>" + data.get("userName") + " (" + data.get("userEmail") + ") " + "have uploaded documents, please have a look and need partner can publish status change.</p>" +
                "<p>Thank you,</p>" +
                "<p>" + appName + "</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        receiverDto.setBody(htmlContent);
        rabbitTemplate.convertAndSend("bargikrit_bigyapan_exchange",
                "bargikrit_bigyapan_routingkey", receiverDto);

        System.out.println("Request sent to RabbitMQ");
    }
}
