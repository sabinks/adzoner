package com.adzoner.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Value("${spring.upload.directory}")
    private String uploadDirectory;
    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/public/advertisements/**")
                .addResourceLocations("file:" + uploadDirectory + "/advertisements/");
        registry
                .addResourceHandler("/public/profile-image/**")
                .addResourceLocations("file:" + uploadDirectory + "/profile_image/");
    }
}