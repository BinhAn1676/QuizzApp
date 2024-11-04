package com.annb.quizz.service;

import com.annb.quizz.dto.response.ParticipantResponse;

public interface ParticipantService {
    ParticipantResponse joinRoom(String roomCode, String username);
}
