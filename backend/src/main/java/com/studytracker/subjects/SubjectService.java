package com.studytracker.subjects;

import com.studytracker.user.CurrentUserService;
import com.studytracker.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final CurrentUserService currentUserService;

    public SubjectService(
            SubjectRepository subjectRepository,
            TopicRepository topicRepository,
            CurrentUserService currentUserService) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> findAll() {
        User user = currentUserService.getCurrentUser();
        return subjectRepository.findAllByOwner(user).stream()
                .map(subject -> new SubjectResponse(
                        subject.getId(),
                        subject.getName(),
                        subject.getTopics().stream()
                                .map(topic -> new TopicResponse(topic.getId(), topic.getName()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        User user = currentUserService.getCurrentUser();
        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setOwner(user);
        Subject saved = subjectRepository.save(subject);
        return new SubjectResponse(saved.getId(), saved.getName(), List.of());
    }

    @Transactional
    public TopicResponse createTopic(TopicRequest request) {
        User user = currentUserService.getCurrentUser();
        Subject subject = subjectRepository
                .findById(request.getSubjectId())
                .filter(s -> s.getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
        Topic topic = new Topic();
        topic.setName(request.getName());
        topic.setSubject(subject);
        topic.setOwner(user);
        Topic saved = topicRepository.save(topic);
        return new TopicResponse(saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> findTopicsBySubject(Long subjectId) {
        User user = currentUserService.getCurrentUser();
        return topicRepository.findAllBySubjectIdAndOwner(subjectId, user).stream()
                .map(topic -> new TopicResponse(topic.getId(), topic.getName()))
                .collect(Collectors.toList());
    }
}
