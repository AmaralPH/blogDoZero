package com.studytracker.sessions;

import java.time.LocalDateTime;

public class StudySessionResponse {

    private Long id;
    private Long subjectId;
    private Long topicId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private long durationMinutes;
    private boolean manualEntry;

    public StudySessionResponse(
            Long id,
            Long subjectId,
            Long topicId,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            long durationMinutes,
            boolean manualEntry) {
        this.id = id;
        this.subjectId = subjectId;
        this.topicId = topicId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationMinutes = durationMinutes;
        this.manualEntry = manualEntry;
    }

    public Long getId() {
        return id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isManualEntry() {
        return manualEntry;
    }
}
