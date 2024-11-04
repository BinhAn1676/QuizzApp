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

}
