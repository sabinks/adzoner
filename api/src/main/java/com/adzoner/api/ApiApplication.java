package com.adzoner.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

	@Value("${server.port}")
	private String serverPort;

	@Value("${spring.profiles.active}")
	private String profilesActive;
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        System.out.println("App Running at " + serverPort + " " + profilesActive );
    }
}
