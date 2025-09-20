package com.studytracker.goals;

import java.time.LocalDate;

public class DailyGoalResponse {

    private LocalDate date;
    private int targetMinutes;

    public DailyGoalResponse(LocalDate date, int targetMinutes) {
        this.date = date;
        this.targetMinutes = targetMinutes;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTargetMinutes() {
        return targetMinutes;
    }
}
