package com.studytracker.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/today")
    public ResponseEntity<StatsResponse> todayStats() {
        return ResponseEntity.ok(statsService.getTodayStats());
    }

    @GetMapping("/week")
    public ResponseEntity<StatsResponse> weekStats() {
        return ResponseEntity.ok(statsService.getLastSevenDaysStats());
    }
}
