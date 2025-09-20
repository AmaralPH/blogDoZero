package com.studytracker.stats;

public class StatsResponse {

    private long totalStudyMinutes;
    private long sessionCount;
    private long attempts;
    private long correctAttempts;
    private int goalTargetMinutes;
    private long goalProgressMinutes;

    public StatsResponse(
            long totalStudyMinutes,
            long sessionCount,
            long attempts,
            long correctAttempts,
            int goalTargetMinutes,
            long goalProgressMinutes) {
        this.totalStudyMinutes = totalStudyMinutes;
        this.sessionCount = sessionCount;
        this.attempts = attempts;
        this.correctAttempts = correctAttempts;
        this.goalTargetMinutes = goalTargetMinutes;
        this.goalProgressMinutes = goalProgressMinutes;
    }

    public long getTotalStudyMinutes() {
        return totalStudyMinutes;
    }

    public long getSessionCount() {
        return sessionCount;
    }

    public long getAttempts() {
        return attempts;
    }

    public long getCorrectAttempts() {
        return correctAttempts;
    }

    public int getGoalTargetMinutes() {
        return goalTargetMinutes;
    }

    public long getGoalProgressMinutes() {
        return goalProgressMinutes;
    }
}
