package com.annb.quizz.service;

import com.annb.quizz.dto.response.ParticipantResponse;
import com.annb.quizz.dto.response.RoomResponse;
import com.annb.quizz.entity.Participant;
import com.annb.quizz.entity.Room;

import java.util.List;

public interface RoomService {
    RoomResponse createRoom(String quizId);

    RoomResponse getByCode(String code);

    List<ParticipantResponse> getParticipants(String roomCode);

    RoomResponse startRoom(String roomId);
}
