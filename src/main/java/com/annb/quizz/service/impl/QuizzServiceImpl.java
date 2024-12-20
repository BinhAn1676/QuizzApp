package com.annb.quizz.service.impl;

import com.annb.quizz.constant.CommonConstant;
import com.annb.quizz.dto.QuizDto;
import com.annb.quizz.dto.QuizSuggestionDTO;
import com.annb.quizz.dto.request.*;
import com.annb.quizz.dto.request.message.BaseContent;
import com.annb.quizz.dto.request.message.BasePart;
import com.annb.quizz.dto.request.message.MessageModel;
import com.annb.quizz.dto.response.AnswerResponse;
import com.annb.quizz.dto.response.QuestionResponse;
import com.annb.quizz.dto.response.QuizResponse;
import com.annb.quizz.dto.response.gemini.Candidate;
import com.annb.quizz.dto.response.gemini.Content;
import com.annb.quizz.dto.response.gemini.GeminiResponse;
import com.annb.quizz.entity.Answer;
import com.annb.quizz.entity.Question;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.AnswerRepository;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.repository.TopicRepository;
import com.annb.quizz.service.AIChatService;
import com.annb.quizz.service.QuizzService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizzServiceImpl implements QuizzService {
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TopicRepository topicRepository;
    private final AIChatService aiChatService;
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
        quiz.setImageUrl(quizDto.getImageUrl());
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
        response.setImageUrl(quiz.getImageUrl());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());
        response.setCreatedBy(quiz.getCreatedBy());
        return response;
    }

    @Override
    public QuizResponse getQuiz(String id) {
        var quiz = quizzRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        QuizResponse response = new QuizResponse();
        response.setTitle(quiz.getTitle());
        response.setId(quiz.getId());
        response.setImageUrl(quiz.getImageUrl());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());
        response.setCreatedBy(quiz.getCreatedBy());
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


    /*@Override
    public Page<QuizDto> filter(BaseFilter req) {
        Pageable pageable = PageRequest.of(req.getPageNo(), req.getPageSize());
        var quizzes = quizzRepository.findFilteredWithStats(req.getTextSearch(), req.getFrom(), req.getTo(), req.getStatus(), pageable);

        return quizzes.map(record -> {
            // Extract fields from the Object[] array
            String quizId = (String) record[0];
            LocalDateTime createdAt = record[1] != null
                    ? ((java.sql.Timestamp) record[1]).toLocalDateTime()
                    : null;
            String createdBy = (String) record[2];
            LocalDateTime updatedAt = record[3] != null
                    ? ((java.sql.Timestamp) record[1]).toLocalDateTime()
                    : null;
            String updatedBy = (String) record[4];
            String description = (String) record[5];
            Integer status = (Integer) record[6];
            String title = (String) record[7];
            String topicId = (String) record[8];  // Assuming topic_id as a String
            String imageUrl = (String) record[9];
            Long reviewCount = ((Number) record[10]).longValue();
            Long questionCount = ((Number) record[11]).longValue();
            Double averageRating = record[12] != null ? ((Number) record[12]).doubleValue() : 0.0;

            // Manually map to QuizDto
            QuizDto dto = new QuizDto();
            dto.setId(quizId);
            dto.setUpdatedAt(updatedAt);
            dto.setUpdatedBy(updatedBy);
            dto.setCreatedAt(createdAt);
            dto.setImageUrl(imageUrl);
            dto.setCreatedBy(createdBy);
            dto.setDescription(description);
            dto.setStatus(status);
            dto.setTitle(title);
            dto.setTopicCode(topicId);
            dto.setReviewCount(reviewCount);
            dto.setQuestionCount(questionCount);
            dto.setAverageRating(averageRating);

            return dto;
        });
    }*/
    @Override
    public Page<QuizDto> filter(BaseFilter req) {
        Pageable pageable = PageRequest.of(req.getPageNo(), req.getPageSize());
        var projections = quizzRepository.findFilteredWithStatsV2(req.getTextSearch(), req.getFrom(), req.getTo(), req.getStatus(), pageable);

        return projections.map(projection -> {
            QuizDto dto = new QuizDto();
            BeanUtils.copyProperties(projection, dto);
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

        // Update basic quiz details
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setStatus(Objects.equals(quizDto.getStatus(), CommonConstant.Status.ACTIVE)
                ? CommonConstant.Status.ACTIVE : CommonConstant.Status.INACTIVE);
        quiz.setTopic(topic);
        quiz.setImageUrl(quizDto.getImageUrl());
        var quizSaved = quizzRepository.saveAndFlush(quiz); // Persist quiz

        // Fetch existing questions for the quiz
        Map<String, Question> existingQuestionsMap = questionRepository.findByQuizzId(quiz.getId())
                .stream().collect(Collectors.toMap(Question::getId, question -> question));

        // Update or create questions
        for (QuestionRequest questionDto : quizDto.getQuestions()) {
            Question question;

            if (questionDto.getId() != null && existingQuestionsMap.containsKey(questionDto.getId())) {
                // Update existing question
                question = existingQuestionsMap.get(questionDto.getId());
            } else {
                // Create new question
                question = new Question();
                question.setId(UUID.randomUUID().toString().replace("-", ""));
                question.setQuizz(quizSaved); // Associate with quiz
            }

            // Set fields for the question
            question.setContent(questionDto.getContent());
            question.setTime(questionDto.getTime());
            question.setPoint(questionDto.getPoint());
            question.setImageUrl(questionDto.getImageUrl());
            question.setQuestionType(questionDto.getQuestionType() == 1
                    ? CommonConstant.QuestionType.MULTIPLE_CHOICE
                    : CommonConstant.QuestionType.WRITE_CHOICE);

            var savedQuestion = questionRepository.save(question);

            // Handle answers
            Map<String, Answer> existingAnswersMap = answerRepository.findByQuestionId(savedQuestion.getId())
                    .stream().collect(Collectors.toMap(Answer::getId, answer -> answer));

            for (AnswerRequest answerDto : questionDto.getAnswers()) {
                Answer answer;

                if (answerDto.getId() != null && existingAnswersMap.containsKey(answerDto.getId())) {
                    // Update existing answer
                    answer = existingAnswersMap.get(answerDto.getId());
                } else {
                    // Create new answer
                    answer = new Answer();
                    answer.setId(UUID.randomUUID().toString().replace("-", ""));
                    answer.setQuestion(savedQuestion); // Associate with question
                }

                // Set fields for the answer
                answer.setContent(answerDto.getContent());
                answer.setIsCorrect(answerDto.getIsCorrect());

                answerRepository.save(answer);
            }
        }

        // Prepare response
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());
        response.setImageUrl(quiz.getImageUrl());
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
                    dto.setTotalQuestions(((Number) row[7]).intValue());
                    return dto;
                })
                .collect(Collectors.toList());

        // Return Page of DTOs
        return new PageImpl<>(suggestions, pageable, resultPage.getTotalElements());
    }

    @Override
    public QuizResponse generateByAI(QuizGenerateRequest request) {
        // Create the prompt for the Gemini API
        String prompt = String.format("Tạo bộ câu hỏi %d câu về chủ đề '%s'. Format JSON chi tiết như sau: %s",
                request.getNumberOfQuestions(), request.getPrompt(), getExpectedJsonFormat());

        // Prepare the message for the Gemini API
        MessageModel message = new MessageModel();
        BasePart basePart = new BasePart(prompt);
        BaseContent baseContent = new BaseContent("user", List.of(basePart));
        message.setContents(List.of(baseContent));

        // Call the Gemini API
        GeminiResponse geminiResponse = aiChatService.sendMessage(message);

        // Validate response
        if (geminiResponse == null || geminiResponse.getCandidates() == null || geminiResponse.getCandidates().isEmpty()) {
            throw new RuntimeException("Không nhận được phản hồi hợp lệ từ Gemini API. Vui lòng thử lại");
        }

        // Convert GeminiResponse to QuizResponse
        return convertGeminiResponseToQuiz(request, geminiResponse);
    }

    private String getExpectedJsonFormat() {
        return "{ \"title\": \"...\", \"topicCode\": \"...\", \"description\": \"...\", \"questions\": [ { \"content\": \"...\", \"questionType\": 1, \"answers\": [ { \"content\": \"...\", \"isCorrect\": true } ] } ] }";
    }

    private QuizResponse convertGeminiResponseToQuiz(QuizGenerateRequest request, GeminiResponse geminiResponse) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Extract text from the first candidate's first part
            List<Candidate> candidates = geminiResponse.getCandidates();
            if (candidates.isEmpty() || candidates.get(0).getContent().getParts().isEmpty()) {
                throw new RuntimeException("Không có phần nội dung hợp lệ từ Gemini API.");
            }

            String geminiContent = candidates.get(0).getContent().getParts().get(0).getText();

            // Clean the JSON string
            String cleanedJson = cleanJsonString(geminiContent);

            // Map the content to QuizResponse
            QuizResponse quizResponse = mapper.readValue(cleanedJson, QuizResponse.class);

            // Set the topicCode from the request
            quizResponse.setTopicCode(request.getTopicCode());

            return quizResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi chuyển đổi phản hồi từ Gemini API.");
        }
    }

    // Helper method to clean the JSON string
    private String cleanJsonString(String rawJson) {
        if (rawJson.startsWith("```json")) {
            rawJson = rawJson.substring(7); // Remove the initial ```json
        }
        if (rawJson.endsWith("```")) {
            rawJson = rawJson.substring(0, rawJson.length() - 3); // Remove the ending ```
        }
        return rawJson.trim(); // Trim any leading/trailing whitespace
    }


}
