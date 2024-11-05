package com.annb.quizz.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.annb.quizz.constant.CommonConstant.*;

@Configuration
public class ConfigCloudinary {

    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", System.getenv("CLOUD_NAME"));
        config.put("api_key", System.getenv("API_KEY"));
        config.put("api_secret", System.getenv("API_SECRET"));
        return new Cloudinary(config);
    }
}