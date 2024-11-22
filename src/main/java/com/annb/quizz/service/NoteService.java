package com.annb.quizz.service;

import com.annb.quizz.dto.request.NoteRequest;
import com.annb.quizz.dto.response.NoteResponse;

import java.util.List;

public interface NoteService {
    List<NoteResponse> create(List<NoteRequest> request);

    List<NoteResponse> getNote(NoteRequest request);

    List<NoteResponse> update(List<NoteRequest> request);

    Boolean delete(String id);
}
