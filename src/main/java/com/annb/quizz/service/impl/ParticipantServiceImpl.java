package com.annb.quizz.service.impl;

import com.annb.quizz.constant.CommonConstant;
import com.annb.quizz.dto.response.ParticipantResponse;
import com.annb.quizz.entity.Participant;
import com.annb.quizz.entity.Room;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.ParticipantRepository;
import com.annb.quizz.repository.RoomRepository;
import com.annb.quizz.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;

    @Override
    public ParticipantResponse joinRoom(String roomCode, String username) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Code", roomCode));
        Participant participant = new Participant();
        participant.setId(UUID.randomUUID().toString().replace("-",""));
        participant.setUsername(username);
        participant.setRoom(room);
        participant.setIsActive(CommonConstant.Status.ACTIVE);
        var saved =  participantRepository.save(participant);
        var res = new ParticipantResponse();
        res.setId(saved.getId());
        res.setUsername(saved.getUsername());
        res.setIsActive(saved.getIsActive());
        return res;
    }
}
