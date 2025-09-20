package com.studytracker.sessions;

import com.studytracker.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    List<StudySession> findAllByOwnerAndStartedAtBetween(User owner, LocalDateTime start, LocalDateTime end);
    List<StudySession> findAllByOwner(User owner);
}
