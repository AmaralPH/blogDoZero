package com.studytracker.sessions;

import com.studytracker.subjects.Subject;
import com.studytracker.subjects.SubjectRepository;
import com.studytracker.subjects.Topic;
import com.studytracker.subjects.TopicRepository;
import com.studytracker.user.CurrentUserService;
import com.studytracker.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final CurrentUserService currentUserService;

    public StudySessionService(
            StudySessionRepository studySessionRepository,
            SubjectRepository subjectRepository,
            TopicRepository topicRepository,
            CurrentUserService currentUserService) {
        this.studySessionRepository = studySessionRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public StudySessionResponse createSession(StudySessionRequest request) {
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

        if (Boolean.TRUE.equals(request.getManualEntry())) {
            if (request.getDurationMinutes() == null) {
                throw new ValidationException("Manual sessions require duration");
            }
        } else if (request.getEndedAt() == null) {
            throw new ValidationException("Timer sessions require an end time");
        }

        StudySession session = new StudySession();
        session.setOwner(user);
        session.setSubject(subject);
        session.setTopic(topic);
        session.setStartedAt(request.getStartedAt());
        session.setEndedAt(request.getEndedAt());
        session.setManualEntry(request.getManualEntry());
        if (request.getDurationMinutes() != null) {
            session.setDurationMinutes(request.getDurationMinutes());
        }
        if (!Boolean.TRUE.equals(request.getManualEntry())) {
            session.updateDurationFromTimes();
        }
        if (session.getDurationMinutes() <= 0) {
            throw new ValidationException("Duration must be positive");
        }
        StudySession saved = studySessionRepository.save(session);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<StudySessionResponse> getSessionsForToday() {
        User user = currentUserService.getCurrentUser();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return studySessionRepository.findAllByOwnerAndStartedAtBetween(user, startOfDay, endOfDay).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudySessionResponse> getSessionsBetween(LocalDateTime start, LocalDateTime end) {
        User user = currentUserService.getCurrentUser();
        return studySessionRepository.findAllByOwnerAndStartedAtBetween(user, start, end).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudySessionResponse> getAllSessions() {
        User user = currentUserService.getCurrentUser();
        return studySessionRepository.findAllByOwner(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private StudySessionResponse toResponse(StudySession session) {
        return new StudySessionResponse(
                session.getId(),
                session.getSubject() != null ? session.getSubject().getId() : null,
                session.getTopic() != null ? session.getTopic().getId() : null,
                session.getStartedAt(),
                session.getEndedAt(),
                session.getDurationMinutes(),
                session.isManualEntry());
    }
}
