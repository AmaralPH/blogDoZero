package com.studytracker.subjects;

import com.studytracker.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByOwner(User owner);
}
