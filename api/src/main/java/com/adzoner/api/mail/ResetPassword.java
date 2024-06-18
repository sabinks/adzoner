package com.adzoner.api.mail;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResetPassword {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${spring.app.name}")
    private String appName;

    public ResetPassword() {
        super();
    }

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        receiverDto.setSubject("Password Reset");
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>Dear User,</h3>" +
                "<p>Please click link below to reset your password</p>" +
                "<a href='" + receiverDto.getUrl() + "'>Reset Password</a>" +
                "<p>Thank you,</p>" +
                "<p>" + appName + "</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        receiverDto.setBody(htmlContent);
        rabbitTemplate.convertAndSend("bargikrit_bigyapan_exchange",
                "bargikrit_bigyapan_routingkey", receiverDto);
        System.out.println("Request sent to RabbitMQ");
//        sendMailService.sendMail(receiverDto);
    }
}
