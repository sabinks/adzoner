package com.adzoner.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

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

	private Map<String, String> data;
}
