package com.annb.quizz.service;

import com.annb.quizz.dto.request.TopicRequest;
import com.annb.quizz.entity.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> getAll();

    Topic getByCode(String code);

    Topic createTopic(TopicRequest topic);
}
