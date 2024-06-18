package com.adzoner.sendmail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverDto {
	private String name;
	private String email;
	private String subject;
	private String body;
	private String url;
}
