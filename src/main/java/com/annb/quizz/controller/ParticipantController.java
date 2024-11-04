package com.annb.quizz.controller;

import com.annb.quizz.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(@RequestParam String roomCode, @RequestParam String username) {
        var participant = participantService.joinRoom(roomCode, username);
        return ResponseEntity.ok(participant);
    }
}