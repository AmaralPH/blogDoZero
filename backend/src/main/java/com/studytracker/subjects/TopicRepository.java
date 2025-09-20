package com.studytracker.subjects;

import com.studytracker.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllBySubjectIdAndOwner(Long subjectId, User owner);
}
