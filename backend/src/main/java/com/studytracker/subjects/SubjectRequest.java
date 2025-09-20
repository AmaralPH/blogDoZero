package com.studytracker.subjects;

import jakarta.validation.constraints.NotBlank;

public class SubjectRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
