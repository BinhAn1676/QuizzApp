package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.message.BaseContent;
import com.annb.quizz.dto.request.message.BasePart;
import com.annb.quizz.dto.request.message.MessageModel;
import com.annb.quizz.dto.response.gemini.GeminiResponse;
import com.annb.quizz.entity.ChatBotLog;
import com.annb.quizz.repository.ChatBotLogRepository;
import com.annb.quizz.service.AIChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIChatServiceImpl implements AIChatService {

    @Value("${gemini.api_url}")
    private String apiUrl;

    @Value("${gemini.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final ChatBotLogRepository chatBotLogRepository;

    @Override
    public GeminiResponse sendMessage(MessageModel req) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MessageModel> requestEntity = new HttpEntity<>(req, headers);

        try {
            ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(
                    apiUrl + "?key=" + apiKey,
                    requestEntity,
                    GeminiResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                GeminiResponse responseBody = response.getBody();

                return responseBody;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<BaseContent> getMessage(BaseFilter req) {
       /* List<ChatBotLog> chatLogs =chatBotLogRepository.findAllByCreatedByOrderByCreatedAt(username);
        var response = chatLogs.stream().map(item ->{
            var baseContent = new BaseContent();
            baseContent.setRole(item.getRole());
            var basePart = new BasePart();
            basePart.setText(item.getMessage());
            baseContent.setParts(Collections.singletonList(basePart));
            return baseContent;
        }).toList();
        return new MessageModel(response);*/
        // Fetch paginated data from the repository
        Pageable pageable = PageRequest.of(req.getPageNo(),req.getPageSize());
        Page<ChatBotLog> chatLogs = chatBotLogRepository.findAllByCreatedByOrderByCreatedAt(req.getTextSearch(), pageable);

        // Map ChatBotLog to BaseContent and return a new Page
        return chatLogs.map(item -> {
            var baseContent = new BaseContent();
            baseContent.setRole(item.getRole());
            var basePart = new BasePart();
            basePart.setText(item.getMessage());
            baseContent.setParts(Collections.singletonList(basePart));
            return baseContent;
        });
    }

    @Override
    @Transactional
    public MessageModel saveMessage(MessageModel req) {
        var response = new MessageModel();
        var baseContents = new ArrayList<BaseContent>();
        for(var content : req.getContents()){
            var item = new ChatBotLog();
            item.setRole(content.getRole());
            item.setMessage(content.getParts().getFirst().getText());
            item.setId(UUID.randomUUID().toString().replace("-",""));
            var saved = chatBotLogRepository.save(item);

            var baseContent = new BaseContent();
            baseContent.setRole(saved.getRole());
            var basePart = new BasePart();
            basePart.setText(saved.getMessage());
            baseContent.setParts(Collections.singletonList(basePart));
            baseContents.add(baseContent);
        }
        response.setContents(baseContents);
        return response;
    }


}
