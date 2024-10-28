package com.annb.quizz.service;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.TopicRequest;
import com.annb.quizz.entity.Topic;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TopicService {
    List<Topic> getAll();

    Topic getByCode(String code);

    Topic createTopic(TopicRequest topic);

    Topic updateByCode(TopicRequest code);

    Page<Topic> getTopics(@Valid BaseFilter request);
}
