package com.studytracker.questions;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.createQuestion(request));
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> listQuestions() {
        return ResponseEntity.ok(questionService.listQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @PostMapping("/{id}/attempts")
    public ResponseEntity<AttemptResponse> createAttempt(
            @PathVariable Long id, @Valid @RequestBody AttemptRequest request) {
        return ResponseEntity.ok(questionService.createAttempt(id, request));
    }

    @GetMapping("/{id}/attempts")
    public ResponseEntity<List<AttemptResponse>> listAttempts(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.listAttempts(id));
    }
}
