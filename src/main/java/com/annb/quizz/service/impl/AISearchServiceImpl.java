package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.message.MessageRequest;
import com.annb.quizz.dto.response.gemini.GeminiResponse;
import com.annb.quizz.service.AISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AISearchServiceImpl implements AISearchService {

    @Value("${gemini.api_url}")
    private String apiUrl;

    @Value("${gemini.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AISearchServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String sendMessage(MessageRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MessageRequest> requestEntity = new HttpEntity<>(req, headers);

        try {
            ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(
                    apiUrl + "?key=" + apiKey,
                    requestEntity,
                    GeminiResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                GeminiResponse responseBody = response.getBody();
                return "";
            } else {
                return "Server error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String sendMessageWithContext(MessageRequest req) {
        return "";
    }
}
