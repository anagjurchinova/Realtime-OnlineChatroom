package com.example.onlinechatroomdemoproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/usr_pfps/**")
                .addResourceLocations("file:///C:/Users/ANA/Desktop/SPRING PROJECT/online-chatroom-demoProject/src/main/resources/static/usr_pfps/");
    }
}
