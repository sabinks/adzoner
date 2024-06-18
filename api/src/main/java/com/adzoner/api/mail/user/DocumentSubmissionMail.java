package com.adzoner.api.mail.user;

import com.adzoner.api.dto.ReceiverDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DocumentSubmissionMail {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.app.name}")
    private String appName;
    @Value("${spring.app.dashboard_url}")
    private String dashboardUrl;

    public void sendMail(ReceiverDto receiverDto) throws Exception {

        receiverDto.setSubject("Thank you for registering with us");
        Map<String, String> data = receiverDto.getData();
        String htmlContent = "<html lang='en'>" +
                "<head></head>" +
                "<body>" +
                "<div>" +
                "<h3>Dear Valued Partner,</h3>" +
                "<p>Thank you for registering with us.</p> " +
                "<p>As a " + data.get("planName") + "member, you have the privilege of uploading up to " + data.get("adsCount") + " advertisements.</p>" +
                "<p>Kindly login to our application and proceed to the dashboard section to upload documents validating your identity (citizenship, passport, license, company registration certificate, etc.). Your ability to publish advertisements will be activated upon successful validation.</p></br>" +
                "<p>Should you require the publication of additional advertisements, please consider upgrading your subscription plan accordingly.</p></br>" +
                "<p>Thank you,</p>" +
                "<a href=" + dashboardUrl + "'  _target='blank'>" + appName + "</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        receiverDto.setBody(htmlContent);
        rabbitTemplate.convertAndSend("bargikrit_bigyapan_exchange",
                "bargikrit_bigyapan_routingkey", receiverDto);

        System.out.println("Request sent to RabbitMQ");
    }
}
