package com.studytracker.goals;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/daily-goal")
public class DailyGoalController {

    private final DailyGoalService dailyGoalService;

    public DailyGoalController(DailyGoalService dailyGoalService) {
        this.dailyGoalService = dailyGoalService;
    }

    @GetMapping
    public ResponseEntity<DailyGoalResponse> getTodayGoal() {
        return ResponseEntity.ok(dailyGoalService.getTodayGoal());
    }

    @PostMapping
    public ResponseEntity<DailyGoalResponse> upsertGoal(@Valid @RequestBody DailyGoalRequest request) {
        return ResponseEntity.ok(dailyGoalService.upsertGoal(request));
    }
}
