package com.studytracker.goals;

import com.studytracker.user.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyGoalRepository extends JpaRepository<DailyGoal, Long> {
    Optional<DailyGoal> findByOwnerAndGoalDate(User owner, LocalDate goalDate);
}
