package com.studytracker.subjects;

public class TopicResponse {
    private Long id;
    private String name;

    public TopicResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
