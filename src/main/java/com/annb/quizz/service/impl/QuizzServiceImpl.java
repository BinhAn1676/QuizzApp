package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.AnswerDto;
import com.annb.quizz.dto.request.QuestionDto;
import com.annb.quizz.dto.request.QuizDto;
import com.annb.quizz.entity.Answer;
import com.annb.quizz.entity.Question;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.repository.AnswerRepository;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class QuizzServiceImpl implements QuizzService {
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public Boolean createQuiz(QuizDto quizDto) throws ExecutionException, InterruptedException {
        Quizz quiz = new Quizz();
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        var quizSaved = quizzRepository.save(quiz);

        // Sử dụng ExecutorService với Virtual Threads để lưu câu hỏi và đáp án
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        // Sử dụng list các Future để theo dõi kết quả của các tác vụ
        List<Future<Void>> futures = new ArrayList<>();

        for (QuestionDto questionDto : quizDto.getQuestions()) {
            // Tạo một task riêng cho mỗi câu hỏi và đáp án
            futures.add(executor.submit(() -> {
                Question question = new Question();
                question.setContent(questionDto.getContent());
                question.setQuizz(quizSaved);
                question.setImageUrl(questionDto.getImageUrl());
                question = questionRepository.save(question);

                // Lưu các đáp án tương ứng với câu hỏi
                for (AnswerDto answerDto : questionDto.getAnswers()) {
                    Answer answer = new Answer();
                    answer.setContent(answerDto.getContent());
                    answer.setIsCorrect(answerDto.getIsCorrect());
                    answer.setQuestion(question);
                    var answerSaved = answerRepository.save(answer);
                }
                return null;
            }));
        }

        // Chờ tất cả các task hoàn thành
        for (Future<Void> future : futures) {
            future.get(); // Nếu có lỗi sẽ throw exception và rollback transaction
        }

        // Đóng ExecutorService
        executor.shutdown();
        System.out.println(quizSaved);
        return true;
    }
}
