package com.annb.quizz.service;

import com.annb.quizz.dto.response.RoomResponse;

public interface RoomService {
    RoomResponse createRoom(String quizId);
}
