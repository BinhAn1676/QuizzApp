package com.annb.quizz.controller;

import com.annb.quizz.dto.request.NoteRequest;
import com.annb.quizz.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody List<NoteRequest> request) {
        return ResponseEntity.ok(noteService.create(request));
    }

    @PostMapping("/get-note")
    public ResponseEntity<?> getNote(@RequestBody NoteRequest request) {
        return ResponseEntity.ok(noteService.getNote(request));
    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody List<NoteRequest> request) {
        return ResponseEntity.ok(noteService.update(request));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id) {
        return ResponseEntity.ok(noteService.delete(id));
    }
}
