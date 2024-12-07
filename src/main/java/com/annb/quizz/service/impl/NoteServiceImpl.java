package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.NoteRequest;
import com.annb.quizz.dto.response.NoteResponse;
import com.annb.quizz.entity.Note;
import com.annb.quizz.repository.NoteRepository;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.service.NoteService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public List<NoteResponse> create(List<NoteRequest> requests) {
        List<NoteResponse> responses = new ArrayList<>();

        for (NoteRequest request : requests) {
            Note note = new Note();
            // Set the quiz if present
            var quiz = quizzRepository.findById(request.getQuizzId());
            quiz.ifPresent(note::setQuizz);

            // Set the question if present
            var question = questionRepository.findById(request.getQuestionId());
            question.ifPresent(note::setQuestion);

            note.setNote(request.getNote());
            note.setTitle(request.getTitle());
            note.setId(UUID.randomUUID().toString().replace("-", ""));

            // Save the note
            var saved = noteRepository.save(note);

            // Map saved note to response
            NoteResponse response = new NoteResponse();
            response.setId(saved.getId());
            response.setNote(saved.getNote());
            response.setCreatedBy(saved.getCreatedBy());
            response.setCreatedDate(saved.getCreatedAt());

            if (!ObjectUtils.isEmpty(saved.getQuestion())) {
                response.setQuestionId(saved.getQuestion().getId());
            }

            if (!ObjectUtils.isEmpty(saved.getQuizz())) {
                response.setQuizzId(saved.getQuizz().getId());
            }

            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getNote(NoteRequest request) {
        if (!StringUtils.hasText(request.getQuizzId()) &&
                !StringUtils.hasText(request.getQuestionId()) &&
                !StringUtils.hasText(request.getUsername())) {
            // Optionally handle all null case
            return Collections.emptyList();
        }

        Specification<Note> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getQuizzId())) {
                predicates.add(criteriaBuilder.equal(root.get("quizz").get("id"), request.getQuizzId()));
            }

            if (StringUtils.hasText(request.getQuestionId())) {
                predicates.add(criteriaBuilder.equal(root.get("question").get("id"), request.getQuestionId()));
            }

            if (StringUtils.hasText(request.getUsername())) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.getUsername()));
            }
            if (StringUtils.hasText(request.getTextSearch())) {
                String searchValue = "%" + request.getTextSearch() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), searchValue),
                        criteriaBuilder.like(root.get("note"), searchValue)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Note> notes = noteRepository.findAll(spec);
        return notes.stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public List<NoteResponse> update(List<NoteRequest> requests) {
        List<String> noteIds = requests.stream()
                .map(NoteRequest::getId) // Ensure NoteRequest has an 'id' field
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Fetch all notes at once
        List<Note> notes = noteRepository.findAllById(noteIds);

        Map<String, NoteRequest> requestMap = requests.stream()
                .collect(Collectors.toMap(NoteRequest::getId, Function.identity()));

        // Update the notes
        notes.forEach(note -> {
            NoteRequest request = requestMap.get(note.getId());
            if (request.getNote() != null) {
                note.setNote(request.getNote());
            }
            if(request.getTitle() != null) {
                note.setTitle(request.getTitle());
            }
            if (request.getQuizzId() != null) {
                quizzRepository.findById(request.getQuizzId()).ifPresent(note::setQuizz);
            }
            if (request.getQuestionId() != null) {
                questionRepository.findById(request.getQuestionId()).ifPresent(note::setQuestion);
            }
        });

        // Save all updated notes
        List<Note> updatedNotes = noteRepository.saveAll(notes);

        // Convert to NoteResponse and return
        return updatedNotes.stream()
                .map(updatedNote -> {
                    NoteResponse response = new NoteResponse();
                    response.setId(updatedNote.getId());
                    response.setNote(updatedNote.getNote());
                    response.setCreatedBy(updatedNote.getCreatedBy());
                    response.setTitle(updatedNote.getTitle());
                    response.setCreatedDate(updatedNote.getCreatedAt());
                    if (updatedNote.getQuestion() != null) {
                        response.setQuestionId(updatedNote.getQuestion().getId());
                    }
                    if (updatedNote.getQuizz() != null) {
                        response.setQuizzId(updatedNote.getQuizz().getId());
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean delete(String id) {
        var isExist = noteRepository
                .existsById(id);
        if (isExist) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private NoteResponse mapToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setNote(note.getNote());
        response.setCreatedBy(note.getCreatedBy());
        response.setCreatedDate(note.getCreatedAt());
        response.setTitle(note.getTitle());
        if (note.getQuestion() != null) {
            response.setQuestionId(note.getQuestion().getId());
        }

        if (note.getQuizz() != null) {
            response.setQuizzId(note.getQuizz().getId());
        }

        return response;
    }

}
