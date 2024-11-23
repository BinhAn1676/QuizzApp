package com.annb.quizz.controller;

import com.annb.quizz.dto.request.ParticipantScorePageRequest;
import com.annb.quizz.dto.request.ParticipantScoreRequest;
import com.annb.quizz.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(@RequestParam String roomCode, @RequestParam String username) {
        var participant = participantService.joinRoom(roomCode, username);
        return ResponseEntity.ok(participant);
    }

    @PostMapping("/save-score")
    public ResponseEntity<?> saveScore(@RequestBody ParticipantScoreRequest request) {
        var participant = participantService.saveScore(request);
        return ResponseEntity.ok(participant);
    }

    @PostMapping("/get-score")
    public ResponseEntity<?> getScore(@RequestBody ParticipantScorePageRequest request) {
        return ResponseEntity.ok(participantService.getScore(request));
    }
}