package com.studytracker.goals;

import jakarta.validation.constraints.Min;

public class DailyGoalRequest {

    @Min(1)
    private Integer targetMinutes;

    public Integer getTargetMinutes() {
        return targetMinutes;
    }

    public void setTargetMinutes(Integer targetMinutes) {
        this.targetMinutes = targetMinutes;
    }
}
