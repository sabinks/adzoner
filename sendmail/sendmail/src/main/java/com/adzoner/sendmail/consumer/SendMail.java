package com.adzoner.sendmail.consumer;

import com.adzoner.sendmail.dto.ReceiverDto;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendMail {
	private final JavaMailSender javaMailSender;

	public SendMail(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Value("${spring.mail.sender}")
	private String mailSender;
	@RabbitListener(queues = "bargikrit_bigyapan_queue")
	public void consumeSendMailQueue(ReceiverDto receiverDto) throws Exception {

		System.out.println("Sending mail: " + receiverDto.getName() + " " + receiverDto.getEmail());
		MimeMessage message = javaMailSender.createMimeMessage();
		message.setFrom(new InternetAddress(mailSender));
		message.setRecipients(MimeMessage.RecipientType.TO, receiverDto.getEmail());
		message.setSubject(receiverDto.getSubject());
		message.setContent(receiverDto.getBody(), "text/html; charset=utf-8");
		javaMailSender.send(message);
	}
}
