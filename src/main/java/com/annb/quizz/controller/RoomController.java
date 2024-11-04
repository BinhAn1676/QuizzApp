package com.annb.quizz.controller;

import com.annb.quizz.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestParam("quizId") String quizId) {
        var room = roomService.createRoom(quizId);
        return ResponseEntity.ok(room);
    }
}
