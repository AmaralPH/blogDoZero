package com.studytracker.subjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TopicRequest {

    @NotNull
    private Long subjectId;

    @NotBlank
    private String name;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
