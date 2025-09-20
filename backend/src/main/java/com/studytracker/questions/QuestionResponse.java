package com.studytracker.questions;

import java.time.Instant;

public class QuestionResponse {
    private Long id;
    private String prompt;
    private String answer;
    private Long subjectId;
    private Long topicId;
    private Instant createdAt;

    public QuestionResponse(Long id, String prompt, String answer, Long subjectId, Long topicId, Instant createdAt) {
        this.id = id;
        this.prompt = prompt;
        this.answer = answer;
        this.subjectId = subjectId;
        this.topicId = topicId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getAnswer() {
        return answer;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
