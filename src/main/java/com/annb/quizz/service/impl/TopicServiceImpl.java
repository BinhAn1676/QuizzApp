package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.TopicRequest;
import com.annb.quizz.entity.Topic;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.TopicRepository;
import com.annb.quizz.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public List<Topic> getAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic getByCode(String code) {
        return topicRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Topic", "code", code));
    }

    @Override
    @Transactional
    public Topic createTopic(TopicRequest topic) {
        Topic newTopic = new Topic();
        newTopic.setCode(topic.getCode());
        newTopic.setTitle(topic.getTitle());
        newTopic.setDescription(topic.getDescription());
        return topicRepository.save(newTopic);
    }
}
