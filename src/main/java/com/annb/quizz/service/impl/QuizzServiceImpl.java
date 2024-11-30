package com.annb.quizz.service.impl;

import com.annb.quizz.constant.CommonConstant;
import com.annb.quizz.dto.QuizDto;
import com.annb.quizz.dto.QuizSuggestionDTO;
import com.annb.quizz.dto.request.*;
import com.annb.quizz.dto.response.AnswerResponse;
import com.annb.quizz.dto.response.QuestionResponse;
import com.annb.quizz.dto.response.QuizResponse;
import com.annb.quizz.entity.Answer;
import com.annb.quizz.entity.Question;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.AnswerRepository;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.repository.TopicRepository;
import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizzServiceImpl implements QuizzService {
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TopicRepository topicRepository;

    @Override
    @Transactional
    public QuizResponse createQuiz(QuizRequest quizDto) {
        // Create and save the quiz
        var topic = topicRepository.findByCode(quizDto.getTopicCode())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "code", quizDto.getTopicCode()));
        Quizz quiz = new Quizz();
        quiz.setId(UUID.randomUUID().toString().replace("-", ""));
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setStatus(CommonConstant.Status.ACTIVE);
        quiz.setTopic(topic);
        var quizSaved = quizzRepository.saveAndFlush(quiz); // Ensure quiz is persisted

        // Loop through each question and save it
        for (QuestionRequest questionDto : quizDto.getQuestions()) {
            Question question = new Question();
            question.setId(UUID.randomUUID().toString().replace("-", ""));
            question.setContent(questionDto.getContent());
            question.setQuizz(quizSaved); // Associate quiz with question
            question.setPoint(questionDto.getPoint());
            question.setTime(questionDto.getTime());
            question.setImageUrl(questionDto.getImageUrl());
            question.setQuestionType(questionDto.getQuestionType() == 1 ? CommonConstant.QuestionType.MULTIPLE_CHOICE : CommonConstant.QuestionType.WRITE_CHOICE);
            var questionSaved = questionRepository.save(question);

            // Save answers corresponding to the question
            for (AnswerRequest answerDto : questionDto.getAnswers()) {
                Answer answer = new Answer();
                answer.setId(UUID.randomUUID().toString().replace("-", ""));
                answer.setContent(answerDto.getContent());
                answer.setIsCorrect(answerDto.getIsCorrect());
                answer.setQuestion(questionSaved); // Associate answer with question
                answerRepository.save(answer);
            }
        }
        QuizResponse response = new QuizResponse();
        response.setId(quizSaved.getId());
        response.setTitle(quiz.getTitle());
        response.setId(quiz.getId());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());

        return response;
    }

    @Override
    public QuizResponse getQuiz(String id) {
        var quiz = quizzRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        QuizResponse response = new QuizResponse();
        response.setTitle(quiz.getTitle());
        response.setId(quiz.getId());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());
        response.setQuestions(
                quiz.getQuestions().stream().map(question -> {
                    var questionDto = new QuestionResponse();
                    questionDto.setId(question.getId());
                    questionDto.setContent(question.getContent());
                    questionDto.setPoint(question.getPoint());
                    questionDto.setTime(question.getTime());
                    questionDto.setQuestionType(question.getQuestionType());
                    questionDto.setImageUrl(question.getImageUrl());
                    questionDto.setAnswers(
                            question.getAnswers().stream().map(answer -> {
                                var answerDto = new AnswerResponse();
                                answerDto.setId(answer.getId());
                                answerDto.setContent(answer.getContent());
                                answerDto.setIsCorrect(answer.getIsCorrect());
                                return answerDto;
                            }).collect(Collectors.toList())
                    );
                    return questionDto;
                }).collect(Collectors.toList())
        );

        return response;
    }

    @Override
    public Page<QuizDto> filter(BaseFilter req) {
        Pageable pageable = PageRequest.of(req.getPageNo(), req.getPageSize());
        var quizzes =  quizzRepository.findFiltered(req.getTextSearch(), req.getFrom(), req.getTo(),req.getStatus(), pageable);
        return quizzes.map(quiz -> {
            QuizDto dto = new QuizDto();
            dto.setId(quiz.getId());
            dto.setTitle(quiz.getTitle());
            dto.setTopicCode(quiz.getTopic().getCode());
            dto.setDescription(quiz.getDescription());
            dto.setStatus(quiz.getStatus());
            return dto;
        });
    }

    @Override
    @Transactional
    public QuizResponse updateQuiz(QuizUpdateRequest quizDto) {
        var topic = topicRepository.findByCode(quizDto.getTopicCode())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "code", quizDto.getTopicCode()));
        Quizz quiz = quizzRepository.findById(quizDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizDto.getId()));
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setStatus(Objects.equals(quizDto.getStatus(), CommonConstant.Status.ACTIVE) ? CommonConstant.Status.ACTIVE : CommonConstant.Status.INACTIVE);
        quiz.setTopic(topic);
        var quizSaved = quizzRepository.saveAndFlush(quiz); // Ensure quiz is persisted
        // Loop through each question and save it
        for (QuestionRequest questionDto : quizDto.getQuestions()) {
            Question question = new Question();
            question.setId(UUID.randomUUID().toString().replace("-", ""));
            question.setContent(questionDto.getContent());
            question.setQuizz(quizSaved); // Associate quiz with question
            question.setTime(questionDto.getTime());
            question.setPoint(questionDto.getPoint());
            question.setImageUrl(questionDto.getImageUrl());
            question.setQuestionType(questionDto.getQuestionType() == 1 ? CommonConstant.QuestionType.MULTIPLE_CHOICE : CommonConstant.QuestionType.WRITE_CHOICE);
            var questionSaved = questionRepository.save(question);

            // Save answers corresponding to the question
            for (AnswerRequest answerDto : questionDto.getAnswers()) {
                Answer answer = new Answer();
                answer.setId(UUID.randomUUID().toString().replace("-", ""));
                answer.setContent(answerDto.getContent());
                answer.setIsCorrect(answerDto.getIsCorrect());
                answer.setQuestion(questionSaved); // Associate answer with question
                answerRepository.save(answer);
            }
        }
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setId(quiz.getId());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());

        return response;
    }

    @Override
    public List<String> getQuestionIds(String id) {
        var quiz = quizzRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        return quiz.getQuestions().stream().map(Question::getId).collect(Collectors.toList());
    }

    @Override
    public QuizRequest parseExcelFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("Sheet1");
            QuizRequest quizRequest = new QuizRequest();
            List<QuestionRequest> questionRequests = new ArrayList<>();
            String currentQuizTitle = null;
            QuestionRequest currentQuestion = null;
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Read quiz details (only set once per quiz)
                String quizTitle = row.getCell(0).getStringCellValue();
                if (currentQuizTitle == null || !currentQuizTitle.equals(quizTitle)) {
                    quizRequest.setTitle(quizTitle);
                    quizRequest.setDescription(row.getCell(1).getStringCellValue());
                    quizRequest.setTopicCode(row.getCell(2).getStringCellValue());
                    currentQuizTitle = quizTitle;
                }
                // Check if a new question has started
                String questionContent = row.getCell(3).getStringCellValue();
                if (currentQuestion == null || !currentQuestion.getContent().equals(questionContent)) {
                    // Create a new QuestionRequest
                    currentQuestion = new QuestionRequest();
                    currentQuestion.setContent(questionContent);
                    currentQuestion.setQuestionType((int) row.getCell(4).getNumericCellValue());
                    // Handle null values for questionPoint and questionTime
                    if (!row.getCell(5).getCellType().equals(CellType.BLANK)) {
                        currentQuestion.setPoint(row.getCell(5).getNumericCellValue());
                    } else {
                        currentQuestion.setPoint((double) 0); // Set a default value if the cell is blank
                    }
                    if (!row.getCell(6).getCellType().equals(CellType.BLANK)) {
                        currentQuestion.setTime((int) row.getCell(6).getNumericCellValue());
                    } else {
                        currentQuestion.setTime(0); // Set a default value if the cell is blank
                    }
                    //currentQuestion.setPoint(row.getCell(5).getNumericCellValue());
                    //currentQuestion.setTime((int) row.getCell(6).getNumericCellValue());
                    if(currentQuestion.getAnswers()==null){
                        currentQuestion.setAnswers(new ArrayList<>());
                    }
                    questionRequests.add(currentQuestion);
                }
                // Read answer details
                AnswerRequest answerRequest = new AnswerRequest();
                CellType cellType = row.getCell(7).getCellType();
                if (cellType == CellType.NUMERIC) {
                    answerRequest.setContent(String.valueOf(row.getCell(7).getNumericCellValue()));
                } else {
                    answerRequest.setContent(row.getCell(7).getStringCellValue());
                }
                answerRequest.setIsCorrect(row.getCell(8).getBooleanCellValue());
                // Associate answer with question
                currentQuestion.getAnswers().add(answerRequest);
            }
            quizRequest.setQuestions(questionRequests);
            workbook.close();
            return quizRequest;
        } catch (Exception e) {
            throw new RuntimeException("Invalid file format. Please upload a valid Excel file (.xls or .xlsx).");
        }
    }

    @Override
    public Boolean deleteById(String id) {
        var isExisted = quizzRepository.existsById(id);
        if(isExisted){
            quizzRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<QuizSuggestionDTO> getTopSuggestedQuizzes(BaseFilter req) {
        Pageable pageable = PageRequest.of(
                req.getPageNo(),
                req.getPageSize()
                );
        var resultPage =  quizzRepository.findSuggestedQuizzes(pageable);
        List<QuizSuggestionDTO> suggestions = resultPage.getContent().stream()
                .map(row -> {
                    QuizSuggestionDTO dto = new QuizSuggestionDTO();
                    dto.setId((String) row[0]);
                    dto.setTitle((String) row[1]);
                    dto.setDescription((String) row[2]);
                    dto.setStatus((Integer) row[3]);
                    dto.setTopicId((String) row[4]);
                    dto.setAverageRating(((Number) row[5]).doubleValue());
                    dto.setTotalComments(((Number) row[6]).intValue());
                    return dto;
                })
                .collect(Collectors.toList());

        // Return Page of DTOs
        return new PageImpl<>(suggestions, pageable, resultPage.getTotalElements());
    }
}
