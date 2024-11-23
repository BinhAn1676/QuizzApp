package com.annb.quizz.service;

import com.annb.quizz.dto.request.ParticipantScorePageRequest;
import com.annb.quizz.dto.request.ParticipantScoreRequest;
import com.annb.quizz.dto.response.ParticipanPageResponse;
import com.annb.quizz.dto.response.ParticipantResponse;
import org.springframework.data.domain.Page;


public interface ParticipantService {
    ParticipantResponse joinRoom(String roomCode, String username);

    ParticipantResponse saveScore(ParticipantScoreRequest request);

    Page<ParticipanPageResponse> getScore(ParticipantScorePageRequest request);
}
