package com.annb.quizz.service;

import com.annb.quizz.dto.response.RoomResponse;
import com.annb.quizz.entity.Room;

public interface RoomService {
    RoomResponse createRoom(String quizId);

    RoomResponse getByCode(String code);
}
