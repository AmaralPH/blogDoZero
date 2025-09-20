package com.studytracker.goals;

import com.studytracker.user.CurrentUserService;
import com.studytracker.user.User;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DailyGoalService {

    private final DailyGoalRepository dailyGoalRepository;
    private final CurrentUserService currentUserService;

    public DailyGoalService(DailyGoalRepository dailyGoalRepository, CurrentUserService currentUserService) {
        this.dailyGoalRepository = dailyGoalRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public DailyGoalResponse upsertGoal(DailyGoalRequest request) {
        if (request.getTargetMinutes() == null) {
            throw new ValidationException("Target minutes is required");
        }
        User user = currentUserService.getCurrentUser();
        LocalDate today = LocalDate.now();
        DailyGoal goal = dailyGoalRepository
                .findByOwnerAndGoalDate(user, today)
                .orElseGet(() -> {
                    DailyGoal g = new DailyGoal();
                    g.setOwner(user);
                    g.setGoalDate(today);
                    return g;
                });
        goal.setTargetMinutes(request.getTargetMinutes());
        DailyGoal saved = dailyGoalRepository.save(goal);
        return new DailyGoalResponse(saved.getGoalDate(), saved.getTargetMinutes());
    }

    @Transactional(readOnly = true)
    public DailyGoalResponse getTodayGoal() {
        User user = currentUserService.getCurrentUser();
        LocalDate today = LocalDate.now();
        return dailyGoalRepository
                .findByOwnerAndGoalDate(user, today)
                .map(goal -> new DailyGoalResponse(goal.getGoalDate(), goal.getTargetMinutes()))
                .orElse(new DailyGoalResponse(today, 0));
    }
}
