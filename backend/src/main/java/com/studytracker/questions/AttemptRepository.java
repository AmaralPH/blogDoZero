package com.studytracker.questions;

import com.studytracker.user.User;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findAllByQuestionAndOwner(Question question, User owner);
    List<Attempt> findAllByOwnerAndAttemptedAtBetween(User owner, Instant start, Instant end);
}
