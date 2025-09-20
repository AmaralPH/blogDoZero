package com.studytracker.questions;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class AttemptRequest {

    @NotNull
    private Boolean correct;

    private String notes;

    private Instant attemptedAt;

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(Instant attemptedAt) {
        this.attemptedAt = attemptedAt;
    }
}
