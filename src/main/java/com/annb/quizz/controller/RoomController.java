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

    @PostMapping("/start")
    public ResponseEntity<?> startRoom(@RequestParam("roomId") String roomId) {
        var room = roomService.startRoom(roomId);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<?> getRoomByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(roomService.getByCode(code));
    }

    @GetMapping("/participants")
    public ResponseEntity<?> getParticipants(@RequestParam("roomCode") String roomCode) {
        return ResponseEntity.ok(roomService.getParticipants(roomCode));
    }
}
