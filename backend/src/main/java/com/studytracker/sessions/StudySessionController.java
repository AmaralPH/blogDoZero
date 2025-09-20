package com.studytracker.sessions;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class StudySessionController {

    private final StudySessionService studySessionService;

    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @PostMapping
    public ResponseEntity<StudySessionResponse> createSession(
            @Valid @RequestBody StudySessionRequest request) {
        return ResponseEntity.ok(studySessionService.createSession(request));
    }

    @GetMapping("/today")
    public ResponseEntity<List<StudySessionResponse>> todaySessions() {
        return ResponseEntity.ok(studySessionService.getSessionsForToday());
    }

    @GetMapping
    public ResponseEntity<List<StudySessionResponse>> sessionsBetween(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(studySessionService.getAllSessions());
        }
        return ResponseEntity.ok(studySessionService.getSessionsBetween(start, end));
    }
}
