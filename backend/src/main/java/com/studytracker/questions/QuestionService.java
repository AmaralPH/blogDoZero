package com.studytracker.questions;

import com.studytracker.subjects.Subject;
import com.studytracker.subjects.SubjectRepository;
import com.studytracker.subjects.Topic;
import com.studytracker.subjects.TopicRepository;
import com.studytracker.user.CurrentUserService;
import com.studytracker.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final CurrentUserService currentUserService;

    public QuestionService(
            QuestionRepository questionRepository,
            AttemptRepository attemptRepository,
            SubjectRepository subjectRepository,
            TopicRepository topicRepository,
            CurrentUserService currentUserService) {
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        User user = currentUserService.getCurrentUser();
        Subject subject = null;
        if (request.getSubjectId() != null) {
            subject = subjectRepository
                    .findById(request.getSubjectId())
                    .filter(s -> s.getOwner().getId().equals(user.getId()))
                    .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
        }
        Topic topic = null;
        if (request.getTopicId() != null) {
            topic = topicRepository
                    .findById(request.getTopicId())
                    .filter(t -> t.getOwner().getId().equals(user.getId()))
                    .orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        }
        Question question = new Question();
        question.setOwner(user);
        question.setPrompt(request.getPrompt());
        question.setAnswer(request.getAnswer());
        question.setSubject(subject);
        question.setTopic(topic);
        Question saved = questionRepository.save(question);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> listQuestions() {
        User user = currentUserService.getCurrentUser();
        return questionRepository.findAllByOwner(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long id) {
        User user = currentUserService.getCurrentUser();
        Question question = questionRepository
                .findByIdAndOwner(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        return toResponse(question);
    }

    @Transactional
    public AttemptResponse createAttempt(Long questionId, AttemptRequest request) {
        User user = currentUserService.getCurrentUser();
        Question question = questionRepository
                .findByIdAndOwner(questionId, user)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        Attempt attempt = new Attempt();
        attempt.setQuestion(question);
        attempt.setOwner(user);
        attempt.setCorrect(Boolean.TRUE.equals(request.getCorrect()));
        attempt.setNotes(request.getNotes());
        if (request.getAttemptedAt() != null) {
            attempt.setAttemptedAt(request.getAttemptedAt());
        }
        Attempt saved = attemptRepository.save(attempt);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AttemptResponse> listAttempts(Long questionId) {
        User user = currentUserService.getCurrentUser();
        Question question = questionRepository
                .findByIdAndOwner(questionId, user)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        return attemptRepository.findAllByQuestionAndOwner(question, user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private QuestionResponse toResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getPrompt(),
                question.getAnswer(),
                question.getSubject() != null ? question.getSubject().getId() : null,
                question.getTopic() != null ? question.getTopic().getId() : null,
                question.getCreatedAt());
    }

    private AttemptResponse toResponse(Attempt attempt) {
        return new AttemptResponse(
                attempt.getId(),
                attempt.getQuestion().getId(),
                attempt.isCorrect(),
                attempt.getNotes(),
                attempt.getAttemptedAt());
    }
}
