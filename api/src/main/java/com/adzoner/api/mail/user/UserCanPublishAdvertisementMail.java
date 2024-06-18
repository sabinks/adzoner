package com.adzoner.api.mail.user;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserCanPublishAdvertisementMail {
    private final RabbitTemplate rabbitTemplate;

    public UserCanPublishAdvertisementMail(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.app.name}")
    private String appName;

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        Map<String, String> data = receiverDto.getData();
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>Dear valued partner,</h3>" +
                "<p>" + data.get("message") + "</p></br>" +
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
