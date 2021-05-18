package JavaExam.repository;

import JavaExam.domain.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepo extends JpaRepository<UserAnswer, Long> {
}
