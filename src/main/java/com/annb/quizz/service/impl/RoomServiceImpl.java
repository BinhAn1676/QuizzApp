package com.annb.quizz.service.impl;

import com.annb.quizz.dto.response.RoomResponse;
import com.annb.quizz.entity.Room;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.repository.RoomRepository;
import com.annb.quizz.service.RoomService;
import com.annb.quizz.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final QuizzRepository quizzRepository;
    @Override
    public RoomResponse createRoom(String quizId) {
        Room room = new Room();
        room.setId(UUID.randomUUID().toString().replace("-",""));
        room.setCode(Utils.generateUniqueCode());
        room.setQuiz(quizzRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId)));
        room.setIsActive(true);
        var saved = roomRepository.save(room);
        var response = new RoomResponse();
        response.setIsActive(saved.getIsActive());
        response.setCode(saved.getCode());
        response.setId(saved.getId());
        return response;
    }

    @Override
    public RoomResponse getByCode(String code) {
        var room = roomRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("Room", "code", code));
        var response = new RoomResponse();
        response.setIsActive(room.getIsActive());
        response.setCode(room.getCode());
        response.setId(room.getId());
        response.setQuizzId(room.getQuiz().getId());
        response.setCreatedBy(room.getCreatedBy());
        return response;
    }

}
