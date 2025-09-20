package com.studytracker.questions;

import java.time.Instant;

public class AttemptResponse {
    private Long id;
    private Long questionId;
    private boolean correct;
    private String notes;
    private Instant attemptedAt;

    public AttemptResponse(Long id, Long questionId, boolean correct, String notes, Instant attemptedAt) {
        this.id = id;
        this.questionId = questionId;
        this.correct = correct;
        this.notes = notes;
        this.attemptedAt = attemptedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getAttemptedAt() {
        return attemptedAt;
    }
}
