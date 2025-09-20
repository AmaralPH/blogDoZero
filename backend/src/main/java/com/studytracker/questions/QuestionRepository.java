package com.studytracker.questions;

import com.studytracker.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOwner(User owner);
    Optional<Question> findByIdAndOwner(Long id, User owner);
}
