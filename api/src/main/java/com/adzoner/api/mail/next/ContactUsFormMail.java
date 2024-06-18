package com.adzoner.api.mail.next;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContactUsFormMail {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${spring.app.name}")
    private String appName;

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        receiverDto.setSubject("Message From Contact Us Page");
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>Dear " + receiverDto.getName() + " Admin</h3>" +
//                "<a href='" + receiverDto.getUrl() + "' target='_blank'>Advertisement Link</a>" +
                "<p>We have received message from contact us form and below are details:-</p>" +
                "<p> Name: " + receiverDto.getData().get("name") + "</p>" +
                "<p> Email: " + receiverDto.getData().get("email") + "</p>" +
                "<p> Phone: " + receiverDto.getData().get("phone") + "</p>" +
                "<p> Subject:" + receiverDto.getData().get("subject") + "</p>" +
                "<p> Message: " + receiverDto.getData().get("message") + "</p>" +
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
