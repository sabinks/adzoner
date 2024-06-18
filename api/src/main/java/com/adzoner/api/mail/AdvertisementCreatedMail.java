package com.adzoner.api.mail;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdvertisementCreatedMail {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${spring.app.name}")
    private String appName;

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        receiverDto.setSubject("New Advertisement Created");
        Map<String, String> data = receiverDto.getData();
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>Dear " + receiverDto.getName() + "</h3>" +
                "<a href='" + receiverDto.getUrl() + "' target='_blank'>Advertisement Link</a>" +
                "<p>" +  data.get("message")+"</p>" +
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
