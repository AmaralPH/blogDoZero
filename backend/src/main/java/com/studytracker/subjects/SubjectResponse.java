package com.studytracker.subjects;

import java.util.List;

public class SubjectResponse {
    private Long id;
    private String name;
    private List<TopicResponse> topics;

    public SubjectResponse(Long id, String name, List<TopicResponse> topics) {
        this.id = id;
        this.name = name;
        this.topics = topics;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TopicResponse> getTopics() {
        return topics;
    }
}
