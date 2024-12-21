package com.annb.quizz.service.impl;

import com.annb.quizz.constant.CommonConstant;
import com.annb.quizz.dto.QuizDto;
import com.annb.quizz.dto.request.ParticipantScorePageRequest;
import com.annb.quizz.dto.request.ParticipantScoreRequest;
import com.annb.quizz.dto.response.ParticipanPageResponse;
import com.annb.quizz.dto.response.ParticipantResponse;
import com.annb.quizz.entity.Participant;
import com.annb.quizz.entity.Room;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.ParticipantRepository;
import com.annb.quizz.repository.RoomRepository;
import com.annb.quizz.service.ParticipantService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public ParticipantResponse joinRoom(String roomCode, String username) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Code", roomCode));
        var participantOptional = participantRepository.findByUsername(username);
        if(participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            participant.setIsActive(CommonConstant.Status.ACTIVE);
            participant.setRoom(room);
            var saved =  participantRepository.save(participant);
            var res = new ParticipantResponse();
            res.setId(saved.getId());
            res.setUsername(saved.getUsername());
            res.setIsActive(saved.getIsActive());
            return res;
        }
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

    @Override
    @Transactional
    public ParticipantResponse saveScore(ParticipantScoreRequest request) {
        var room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room", "Code", request.getRoomId()));
        Participant participant = room.getParticipants().stream()
                .filter(item -> item.getUsername().equalsIgnoreCase(request.getUsername()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Participant", "Username", request.getUsername()));
        participant.setScore(request.getScore());
        var saved = participantRepository.save(participant);
        var response =  new ParticipantResponse();
        response.setId(saved.getId());
        response.setUsername(saved.getUsername());
        response.setIsActive(saved.getIsActive());
        response.setScore(saved.getScore());
        return response;
    }

    @Override
    public Page<ParticipanPageResponse> getScore(ParticipantScorePageRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());
        var scores =  participantRepository.findFiltered(request.getTextSearch(), request.getRoomId(),pageable);
        return scores.map(item -> {
            var dto = new ParticipanPageResponse();
            dto.setId(item.getId());
            dto.setUsername(item.getUsername());
            dto.setIsActive(item.getIsActive());
            dto.setScore(item.getScore());
            dto.setCreatedAt(item.getCreatedAt());
            return dto;
        });
    }

    @Override
    public ParticipantResponse leaveRoom(String roomCode, String username) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Code", roomCode));
        Participant participant = participantRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "username", username));
        participant.setIsActive(CommonConstant.Status.INACTIVE);
        participant.setRoom(null);
        var saved =  participantRepository.save(participant);
        var res = new ParticipantResponse();
        res.setId(saved.getId());
        res.setUsername(saved.getUsername());
        res.setIsActive(saved.getIsActive());
        return res;
    }
}
