package com.studytracker.stats;

import com.studytracker.goals.DailyGoalRepository;
import com.studytracker.sessions.StudySession;
import com.studytracker.sessions.StudySessionRepository;
import com.studytracker.user.CurrentUserService;
import com.studytracker.user.User;
import com.studytracker.questions.AttemptRepository;
import com.studytracker.questions.Attempt;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatsService {

    private final StudySessionRepository studySessionRepository;
    private final AttemptRepository attemptRepository;
    private final DailyGoalRepository dailyGoalRepository;
    private final CurrentUserService currentUserService;

    public StatsService(
            StudySessionRepository studySessionRepository,
            AttemptRepository attemptRepository,
            DailyGoalRepository dailyGoalRepository,
            CurrentUserService currentUserService) {
        this.studySessionRepository = studySessionRepository;
        this.attemptRepository = attemptRepository;
        this.dailyGoalRepository = dailyGoalRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public StatsResponse getTodayStats() {
        LocalDate today = LocalDate.now();
        return buildStats(today, today);
    }

    @Transactional(readOnly = true)
    public StatsResponse getLastSevenDaysStats() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        return buildStats(start, end);
    }

    private StatsResponse buildStats(LocalDate start, LocalDate end) {
        User user = currentUserService.getCurrentUser();
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();

        List<StudySession> sessions =
                studySessionRepository.findAllByOwnerAndStartedAtBetween(user, startDateTime, endDateTime);
        long totalMinutes = sessions.stream().mapToLong(StudySession::getDurationMinutes).sum();
        long sessionCount = sessions.size();

        Instant startInstant = startDateTime.toInstant(ZoneOffset.UTC);
        Instant endInstant = endDateTime.toInstant(ZoneOffset.UTC);
        List<Attempt> attempts =
                attemptRepository.findAllByOwnerAndAttemptedAtBetween(user, startInstant, endInstant);
        long attemptCount = attempts.size();
        long correct = attempts.stream().filter(Attempt::isCorrect).count();

        int goalTarget = 0;
        if (start.equals(end)) {
            goalTarget = dailyGoalRepository
                    .findByOwnerAndGoalDate(user, start)
                    .map(g -> g.getTargetMinutes())
                    .orElse(0);
        }

        return new StatsResponse(totalMinutes, sessionCount, attemptCount, correct, goalTarget, totalMinutes);
    }
}
